package org.neo.util;

import org.neo.util.MemberDef;
import org.neo.util.ClassDef;
import java.lang.reflect.Method;

/**
 *
 * @author theninger
 */
public class MethodDef extends MemberDef {

    private ClassDef[] argTypes;

    public MethodDef(Method method) {
        super(ClassDef.get(method.getDeclaringClass()), method.getName(), ClassDef.get(method.getReturnType()), method.getModifiers());
        final Class<?>[] parameterTypes = method.getParameterTypes();
        this.argTypes = new ClassDef[parameterTypes.length];
        for (int ix = 0; ix < parameterTypes.length; ix++) {
            this.argTypes[ix] = ClassDef.get(parameterTypes[ix]);
        }
    }

    public MethodDef(ClassDef owner, String name, ClassDef returnType, int modifiers, ClassDef[] argTypes) {
        super(owner, name, returnType, modifiers);
        this.argTypes = argTypes;
    }

    public boolean isCallableWith(String...argTypes) {
        int length = this.argTypes.length;
        if (argTypes.length != length) return false;
        for (int ix = 0; ix < length; ix++) {
            if (argTypes[ix] != null && !this.argTypes[ix].isAssignableFrom(argTypes[ix])) return false;
        }
        return true;
    }
    
//    public static String signature(Method method) {
//        StringBuilder buff = new StringBuilder();
//        buff.append(method.getName());
//        buff.append('(');
//        Class[] types = method.getParameterTypes();
//        String comma = "";
//        for (Class type : types) {
//            buff.append(comma);
//            comma = ",";
//            buff.append(type.getSimpleName());
//        }
//        buff.append(')');
//        return buff.toString();
//    }
//
//    public String signature() {
//        StringBuilder buff = new StringBuilder(name);
//        buff.append('(');
//        String comma = "";
//        if (argTypes != null) {
//            for (ClassDef type : argTypes) {
//                buff.append(comma);
//                comma = ",";
//                if (type != null) buff.append(type.getName());
//            }
//        }
//        buff.append(')');
//        return buff.toString();
//    }

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder("def ");
        buff.append(getName());
        if (getReturnType() != null) {
            buff.append('~');
            buff.append(getReturnType().getName());
        }
        buff.append('(');
        String comma = "";
        if (argTypes != null) {
            for (ClassDef type : argTypes) {
                buff.append(comma);
                comma = ", ";
                if (type != null) buff.append(type.getName());
            }
        }
        buff.append(')');
        return buff.toString();
    }

}
