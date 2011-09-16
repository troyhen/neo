package neo.lang;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.neo.NeoException;

/**
 *
 * @author Troy Heninger
 */
public class Invoke {

    private Method classMethod;
    private Method packageMethod;
    
    public Object invoke(Object object, String name, Object...args) {
        find(object, name, args);
        try {
            if (classMethod != null) {
                return classMethod.invoke(object, args);
            }
            Object[] args2 = staticArgs(object, args);
            return packageMethod.invoke(null, args2);
        } catch (NeoException e) {
            throw e;
        } catch (Exception ex) {
            Logger.getLogger(Invoke.class.getName()).log(Level.SEVERE, null, ex);
            throw new NeoException(ex);
        }
    }

    public void find(Object object, String name, Object...args) {
        Class type = object.getClass();
        classMethod = lookup2(type, name, args);
        if (classMethod != null) return;
        Object[] args2 = staticArgs(object, args);
        Package[] pkgs = Package.getPackages();
        for (Package pkg : pkgs) {
            try {
                type = Class.forName(pkg.getName() + ".N");
                packageMethod = lookup2(type, name, args2);
                if (packageMethod != null) return;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Invoke.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private Object[] staticArgs(Object object, Object[] args) {
        Object[] args2 = new Object[args.length + 1];
        args2[0] = object;
        System.arraycopy(args, 0, args2, 1, args.length);
        return args2;
    }

    public static Method lookup(Class type, String name, Object...args) {
        Method meth = null;
        try {
            Class[] types = new Class[args.length];
            int ix = 0;
            for (Object arg : args) {
                types[ix++] = arg == null ? Object.class : arg.getClass();
            }
            meth = type.getMethod(name, types);
        } catch (NeoException e) {
            throw e;
        } catch (Exception ex) {
            Logger.getLogger(Invoke.class.getName()).log(Level.SEVERE, null, ex);
            throw new NeoException(ex);
        }
        return meth;
    }

    public static Method lookup2(Class type, String name, Object...args) {
        Method meth = null;
        Method[] meths = type.getMethods();
    loop:
        for (Method method : meths) {
            if (!name.equals(method.getName())) continue;
            Class[] parms = method.getParameterTypes();
            if (parms.length != args.length) continue;
            for (int ix = 0, iz = args.length; ix < iz; ix++) {
                Class parm = parms[ix];
                Object arg = args[ix];
                if (!isCompatible(arg, parm)) continue loop;
            }
            meth = method;
            break;  // TODO see if a better match exists
        }
        return meth;
    }

    private static boolean isCompatible(Object arg, Class parm) {
        if (arg == null) return true;
        Class argc = arg.getClass();
        if (parm == argc) return true;
        if (parm.isPrimitive()) {
            if (parm == Boolean.TYPE && argc == Boolean.class) return true;
            if (parm == Byte.TYPE && argc == Byte.class) return true;
            if (parm == Character.TYPE && argc == Character.class) return true;
            if (parm == Double.TYPE && argc == Double.class) return true;
            if (parm == Float.TYPE && argc == Float.class) return true;
            if (parm == Integer.TYPE && argc == Integer.class) return true;
            if (parm == Long.TYPE && argc == Long.class) return true;
            if (parm == Short.TYPE && argc == Short.class) return true;
            return false;
        }
        return parm.isAssignableFrom(argc);
    }

}
