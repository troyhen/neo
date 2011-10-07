package org.neo.back;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaControlIf extends JavaStatementIf {

    @Override
    protected Node getConditionLeg(Node node) {
        return node.getFirst();
    }

    protected Node getElseLeg(Node node) {
        return node.get(2);
    }

    @Override
    protected Node getMainLeg(Node node) {
        return node.get(1);
    }

//    @Override
//    public void render(Node node) {
//        Node parent = node.getParent();
//        if (parent.isNamed("statement")) {
//            super.render(node);
//        } else {
//            renderExpression(node);
//        }
//    }

    @Override
    protected void renderCondition(Node node, CodeBuilder buff, boolean needBoolean) {
        renderConditionLeg(getConditionLeg(node), buff, needBoolean);
    }

//    protected void renderExpression(Node node) {
//        boolean needBoolean = !"boolean".equals(getConditionLeg(node).getTypeName());
//        CodeBuilder buff = JavaCompilation.output();
//        buff
//        throw new UnsupportedOperationException("Not yet implemented");
//    }

    @Override
    protected void renderTail(Node node, CodeBuilder buff) {
        Node mainLeg = getMainLeg(node);
        Node elseLeg = getElseLeg(node);
        if (elseLeg != null) {
            buff.append(") {").eol().tabMore();
            mainLeg.render("java");
            buff.tabLess().tab().append("}");
            elseLeg.render("java");
        } else {
            super.renderTail(node, buff);
        }
    }

}
