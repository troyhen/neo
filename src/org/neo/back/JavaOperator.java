package org.neo.back;

import org.neo.Node;
import org.neo.back.JavaCompilation.Segment;

/**
 *
 * @author Troy Heninger
 */
public class JavaOperator implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output(Segment.inside);
        buff.append("(");
        node.get(0).render("java");
        buff.append(" ");
        buff.append(node.getValue());
        buff.append(" ");
        node.get(1).render("java");
        buff.append(")");
    }

}
