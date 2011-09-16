package org.neo.back;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaReferenceThis implements Backend {

    @Override
    public void render(Node node) {
        node.getFirst().render("java");
    }

}
