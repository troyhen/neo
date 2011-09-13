package org.neo.back;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaOperatorAs implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output();
        buff.append("to");
        if (node.get(2) == null && node.getType().indexOf('.') < 0) {
            buff.append(node.getType()).append("(");
            node.get(0).render("java");
            buff.append(")");
        } else {
            buff.append("(");
            node.get(0).render("java");
            buff.append(", (");
            JavaStatementImport imp = new JavaStatementImport();
            buff.append(node.getType()).append(") null)");
        }
    }

}
