package org.neo.back;

import org.neo.Node;
import org.neo.back.JavaCompilation.Segment;

/**
 *
 * @author Troy Heninger
 */
public class JavaOperatorAs implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output(Segment.inside);
        buff.append("to");
        if (node.get(2) == null) {
            buff.append(node.get(1).getValue());
            buff.append("(");
            node.get(0).render("java");
            buff.append(")");
        } else {
            buff.append("(");
            node.get(0).render("java");
            buff.append(", (");
            JavaStatementImport imp = new JavaStatementImport();
            buff.append(imp.getPath(node.get(1)));
            buff.append(") null)");
        }
    }

}
