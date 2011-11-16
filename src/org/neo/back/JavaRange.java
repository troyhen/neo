package org.neo.back;

import org.neo.core.Range;
import org.neo.parse.Node;

/**
 * @author Troy Heninger
 */
public class JavaRange implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output();
        buff.append("new neo.lang.Range(");
        node.getFirst().render("java");
        buff.append(", ");
        node.get(1).render("java");
        buff.append(", ");
        if (node.isNamed(Range.RANGE_INCLUSIVE)) buff.append("true");
        else buff.append("false");
        Node step = node.get(2);
        if (step != null) {
            buff.append(", ");
            step.render("java");
        }
        buff.append(")");
    }

}
