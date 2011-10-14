package org.neo.back;

import javassist.ClassPool;
import javassist.CtClass;
import org.neo.parse.Node;

/**
 *
 * @author Troy Heninger
 */
public class JvmCompilation implements Backend {

    public static ThreadLocal<CtClass> output = new ThreadLocal<CtClass>();

    @Override
    public void render(Node node) {
        ClassPool pool = ClassPool.getDefault();
    }

}
