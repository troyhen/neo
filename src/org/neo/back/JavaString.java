package org.neo.back;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaString implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output();
        if (node.getFirst() != null) {
            node = node.getFirst();
            String add = "";
            while (node != null) {
                buff.append(add);
                add = " + ";
                if (!node.getName().startsWith("string")) {
                    buff.append("(");
                    node.render("java");
                    buff.append(")");
                } else node.render("java");
                node = node.getNext();
            }
        } else {
            buff.append("\"");
            buff.append(node.getValue());   // TODO escape double quotes
            buff.append("\"");
        }
    }

}
