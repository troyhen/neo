package org.neo.back;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaArray implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output();
        buff.append("new ").append(node.getTypeName()).append(" {").eol().tabMore().tab();
        node = node.getFirst();
        String comma = "";
        while (node != null) {
            buff.append(comma);
            comma = ", ";
            node.render("java");
            node = node.getNext();
        }
        buff.eol().tabLess().tab().append("}");
    }

}
