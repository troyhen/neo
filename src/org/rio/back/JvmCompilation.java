package org.rio.back;

import javassist.ClassPool;
import javassist.CtClass;
import org.rio.Node;

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
