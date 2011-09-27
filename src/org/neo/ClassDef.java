package org.neo;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Troy Heninger
 */
public class ClassDef {

    private static Map<String, ClassDef> map = new HashMap<String, ClassDef>();

    private String name;
    private String simpleName;
    private ClassDef superClass;
    private ClassDef[] interfaces;
    private MethodDef[] methods;
    private FieldDef[] fields;
    private Class type;

    public static ClassDef get(Class type) {
        if (type == null) return null;
        ClassDef def = map.get(type.getName());
        if (def == null) {
            def = new ClassDef(type);
        }
        return def;
    }

    public static ClassDef get(String name) {
        if (name == null) throw new NullPointerException("missing class name");
        ClassDef type = map.get(name);
        if (type == null) {
            int arrays = 0;
            while (name.endsWith("[]")) {
                name = name.substring(0, name.length() - 2);
                arrays++;
            }
            try {
                type = get(Compiler.compiler().loadClass(name));
            } catch (ClassNotFoundException ex) {
                if (name.contains(".")) {
                    Log.info(ex.toString());
                    return null;
                }
                return Compiler.compiler().symbolFind(name);
            }
            while (arrays-- > 0) {
                type = get(Array.newInstance(type.type, 0).getClass());
            }
        }
        return type;
    }

    private ClassDef(Class type) {
        this.name = type.getName();
        map.put(this.name, this);
        this.simpleName = type.getSimpleName();
        this.superClass = get(type.getSuperclass());
        final Class[] interfaces1 = type.getInterfaces();
        this.interfaces = new ClassDef[interfaces1.length];
        for (int ix = 0; ix < interfaces1.length; ix++) {
            this.interfaces[ix] = get(interfaces1[ix]);
        }
        final Method[] methods1 = type.getDeclaredMethods();
        this.methods = new MethodDef[methods1.length];
        for (int ix = 0; ix < methods1.length; ix++) {
            this.methods[ix] = new MethodDef(methods1[ix]);
        }
        final Field[] fields1 = type.getDeclaredFields();
        this.fields = new FieldDef[fields1.length];
        for (int ix = 0; ix < fields1.length; ix++) {
            this.fields[ix] = new FieldDef(fields1[ix]);
        }
        this.type = type;
    }

    public MemberDef findField(String symbol) {
        for (FieldDef field : fields) {
            if (field.getName().equals(symbol)) return field;
        }
        return null;
    }

    public MethodDef findMethod(String name, String[] argTypes) {
        for (MethodDef method : methods) {
            if (method.getName().equals(name) && method.isCallableWith(argTypes)) {
                return method;
            }
        }
        return null;
    }

    public ClassDef[] getInterfaces() {
        return interfaces;
    }

    public FieldDef[] getFields() {
        return fields;
    }

    public MethodDef[] getMethods() {
        return methods;
    }

    public String getName() {
        return name;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public ClassDef getSuperClass() {
        return superClass;
    }

    public boolean isAssignableFrom(String typeName) {
        if (typeName == null) throw new NullPointerException("missing typeName");
        ClassDef type = get(typeName);
        return isAssignableFrom(type);
    }

    public boolean isAssignableFrom(ClassDef type) {
        if (this == type || this.name.equals(type.name)) return true;
        if (this.name.equals("java.lang.Integer")   && type.name.equals("int"))     return true;
        if (type.name.equals("java.lang.Integer")   && this.name.equals("int"))     return true;
        if (this.name.equals("java.lang.Long")      && type.name.equals("long"))    return true;
        if (type.name.equals("java.lang.Long")      && this.name.equals("long"))    return true;
        if (this.name.equals("java.lang.Float")     && type.name.equals("float"))   return true;
        if (type.name.equals("java.lang.Float")     && this.name.equals("float"))   return true;
        if (this.name.equals("java.lang.Double")    && type.name.equals("double"))  return true;
        if (type.name.equals("java.lang.Double")    && this.name.equals("double"))  return true;
        if (this.name.equals("java.lang.Byte")      && type.name.equals("byte"))    return true;
        if (type.name.equals("java.lang.Byte")      && this.name.equals("byte"))    return true;
        if (this.name.equals("java.lang.Boolean")   && type.name.equals("boolean")) return true;
        if (type.name.equals("java.lang.Boolean")   && this.name.equals("boolean")) return true;
        if (this.name.equals("java.lang.Character") && type.name.equals("char"))    return true;
        if (type.name.equals("java.lang.Character") && this.name.equals("char"))    return true;
        if (this.name.equals("java.lang.Short")     && type.name.equals("short"))   return true;
        if (type.name.equals("java.lang.Short")     && this.name.equals("short"))   return true;
        for (ClassDef interf : type.interfaces) {
            boolean result = isAssignableFrom(interf);
            if (result) return true;
        }
        if (type.superClass != null) return isAssignableFrom(type.superClass);
        return false;
    }

    @Override
    public String toString() {
        return "class " + name;
    }

}
