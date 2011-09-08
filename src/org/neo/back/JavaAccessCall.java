package org.neo.back;

import org.neo.Node;
import org.neo.back.JavaCompilation.Segment;

/**
 *
 * @author Troy Heninger
 */
public class JavaAccessCall implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output(Segment.inside);
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
