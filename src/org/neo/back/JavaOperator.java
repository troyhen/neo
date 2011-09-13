package org.neo.back;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaOperator implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output();
        boolean needParens = node.getParent().getName().startsWith("operator");
        if (node.getValue().toString().equals("<=>")) renderCompare(node, buff);
        else {
            if (needParens) buff.append("(");
            node.get(0).render("java");
            buff.append(" ");
            buff.append(node.getValue());
            buff.append(" ");
            node.get(1).render("java");
            if (needParens) buff.append(")");
        }
    }

    private void renderCompare(Node node, CodeBuilder buff) {
        buff.append("compare(");
        node.get(0).render("java");
        buff.append(", ");
        node.get(1).render("java");
        buff.append(")");
    }

}
