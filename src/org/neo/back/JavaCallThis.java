package org.neo.back;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaCallThis implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output();
        node = node.getFirst();
        node.render("java");
        node = node.getNext();
        buff.append("(");
        String comma = "";
        while (node != null) {
            buff.append(comma);
            comma = ", ";
            node.render("java");
            node = node.getNext();
        }
        buff.append(")");
    }

}
