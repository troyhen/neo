package org.neo.back;

import org.neo.parse.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaOperator implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output();
        boolean needParens = node.getParent().isNamed("operator");
        String operator = node.getValue().toString();
        if (operator.equals("<=>")) {
            renderCompare(node, buff);
            return;
        } else if (operator.equals("==")) {
            renderEqual(node, buff);
            return;
        } else if (operator.equals("~=")) {
            renderMatch(node, buff);
            return;
        } else if (operator.equals("===")) {
            operator = "==";
        }
        if (needParens) buff.append("(");
        node.get(0).render("java");
        buff.append(" ");
        buff.append(operator);
        buff.append(" ");
        node.get(1).render("java");
        if (needParens) buff.append(")");
    }

    private void renderCompare(Node node, CodeBuilder buff) {
        buff.append("compare(");
        node.get(0).render("java");
        buff.append(", ");
        node.get(1).render("java");
        buff.append(")");
    }

    private void renderEqual(Node node, CodeBuilder buff) {
        buff.append("equal(");
        node.get(0).render("java");
        buff.append(", ");
        node.get(1).render("java");
        buff.append(")");
    }

    private void renderMatch(Node node, CodeBuilder buff) {
        buff.append("match(");
        node.get(0).render("java");
        buff.append(", ");
        node.get(1).render("java");
        buff.append(")");
    }

}
