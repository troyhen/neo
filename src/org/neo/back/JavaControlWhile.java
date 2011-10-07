package org.neo.back;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaControlWhile extends JavaControlIf {

    @Override
    public void render(Node node) {
        if (getElseLeg(node) != null) {
            renderWithElse(node);
            return;
        }
        super.render(node);
    }

    @Override
    protected void renderHead(CodeBuilder buff) {
        buff.append("while (");
    }

    protected void renderWithElse(Node node) {
        CodeBuilder buff = JavaCompilation.output();
        boolean needBoolean = !"boolean".equals(getConditionLeg(node).getTypeName());
        buff.append("if (");
        renderCondition(node, buff, needBoolean);
        buff.append(") {").eol().tabMore();
        buff.tab().append("do {").eol().tabMore();
        getMainLeg(node).render("java");
        buff.tabLess().tab().append("} while (");
        renderCondition(node, buff, needBoolean);
        buff.append(");").eol();
        buff.tabLess().tab().append("}");
        getElseLeg(node).render("java");
        buff.tabLess().tab().append("}").eol();
    }

//    public void renderExpression(Node node, Node expression) {
//        CodeBuilder buff = JavaCompilation.output();
//        boolean needBoolean = !"boolean".equals(node.getFirst().getTypeName());
//        buff.append("if (");
//        if (needBoolean) buff.append("toboolean(");
//        node.getFirst().render("java");
//        if (needBoolean) buff.append(")");
//        buff.append(") {").eol().tabMore();
//        node.get(1).render("java");
//        buff.tabLess().tab().append("}");
//        if (node.get(2) != null) {
//            node.get(2).render("java");
//        } else {
//            buff.eol();
//        }
//    }

}
