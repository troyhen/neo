package org.neo.back;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaStatements implements Backend {

    @Override
    public void render(Node node) {
        node = node.getFirst();
        while (node != null) {
            node.render("java");
            node = node.getNext();
        }
    }

}
