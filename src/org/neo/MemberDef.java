package org.neo;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 *
 * @author Troy Heninger
 */
public class MemberDef {

    private ClassDef owner;
    private String name;
    private ClassDef returnType;
    private int modifiers;

    public MemberDef(ClassDef owner, String name, ClassDef returnType, int modifiers) {
        this.owner = owner;
        this.name = name;
        this.returnType = returnType;
        this.modifiers = modifiers;
    }

    public String getName() {
        return name;
    }

    public ClassDef getOwner() {
        return owner;
    }

    public ClassDef getReturnType() {
        return returnType;
    }

    public boolean isPrivate() {
        return Modifier.isPrivate(modifiers);
    }

    public boolean isProtected() {
        return Modifier.isProtected(modifiers);
    }

    public boolean isPublic() {
        return Modifier.isPublic(modifiers);
    }

    public boolean isStatic() {
        return Modifier.isStatic(modifiers);
    }

}
