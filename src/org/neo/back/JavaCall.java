package org.neo.back;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaCall implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output();
        node = node.getFirst();
        node.render("java");
        node = node.getNext();
        buff.append("(");
        while (node != null) {
            node.render("java");
            node = node.getNext();
        }
        buff.append(")");
    }

}
