package org.neo;

import java.lang.reflect.Field;

/**
 *
 * @author theninger
 */
public class FieldDef extends MemberDef {

    public FieldDef(Field field) {
        super(ClassDef.get(field.getDeclaringClass()), field.getName(), ClassDef.get(field.getType()), field.getModifiers());
    }

    public FieldDef(ClassDef owner, String name, ClassDef type, int modifiers) {
        super(owner, name, type, modifiers);
    }

    @Override
    public String toString() {
        if (isStatic()) return "val " + getName() + '~' + getReturnType().getName();
        return "var " + getName() + '~' + getReturnType().getName();
    }

}
