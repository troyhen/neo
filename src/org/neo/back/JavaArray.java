package org.neo.back;

import org.neo.Node;
import org.neo.back.JavaCompilation.Segment;

/**
 *
 * @author Troy Heninger
 */
public class JavaArray implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output(Segment.inside);
        buff.append("new ").append(node.getType()).append(" {").eol().tabMore().tab();
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
