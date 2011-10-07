package org.neo.back;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaStatementUntil extends JavaStatementWhile {

//    @Override
//    public void render(Node node) {
//        CodeBuilder buff = JavaCompilation.output();
//        boolean needBoolean = !"boolean".equals(node.get(1).getTypeName());
//        buff.tab().append("while (");
//        renderCondition(node, buff, needBoolean);
//        buff.append(") {").eol().tabMore();
//        node.getFirst().render("java");
//        buff.tabLess().tab().append("}").eol();
//    }
//
    @Override
    protected void renderCondition(Node node, CodeBuilder buff, boolean needBoolean) {
        JavaStatementUnless.renderConditionLeg(getConditionLeg(node), buff, needBoolean);
    }

}
