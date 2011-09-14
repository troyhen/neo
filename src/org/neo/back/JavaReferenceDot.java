package org.neo.back;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaReferenceDot implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output();
        node.get(0).render("java");
        buff.append(".");
        node.get(1).render("java");
    }

}
