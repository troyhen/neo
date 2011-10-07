package org.neo.back;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaStatementIf implements Backend {

    @Override
    public void render(Node node) {
        boolean needBoolean = !"boolean".equals(getConditionLeg(node).getTypeName());
        CodeBuilder buff = JavaCompilation.output();
        if (!node.getParent().isNamed("statement")) buff.tab();
        renderHead(buff);
        renderCondition(node, buff, needBoolean);
        renderTail(node, buff);
    }

    protected Node getConditionLeg(Node node) {
        return node.get(1);
    }

    protected Node getMainLeg(Node node) {
        return node.getFirst();
    }

    protected void renderCondition(Node node, CodeBuilder buff, boolean needBoolean) {
        renderConditionLeg(node.get(1), buff, needBoolean);
    }

    static void renderConditionLeg(Node legNode, CodeBuilder buff, boolean needBoolean) {
        if (needBoolean) buff.append("toboolean(");
        legNode.render("java");
        if (needBoolean) buff.append(")");
    }

    protected void renderHead(CodeBuilder buff) {
        buff.append("if (");
    }

    protected void renderTail(Node node, CodeBuilder buff) {
        buff.append(") {").eol().tabMore();
        getMainLeg(node).render("java");
        buff.tabLess().tab().append("}").eol();
    }

}
