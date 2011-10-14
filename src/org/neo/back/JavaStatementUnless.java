package org.neo.back;

import org.neo.parse.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaStatementUnless extends JavaStatementIf {

    @Override
    protected void renderCondition(Node node, CodeBuilder buff, boolean needBoolean) {
        renderConditionLeg(getConditionLeg(node), buff, needBoolean);
    }

    static void renderConditionLeg(Node legNode, CodeBuilder buff, boolean needBoolean) {
        buff.append("!");
        if (needBoolean) buff.append("toboolean");
        buff.append("(");
        legNode.render("java");
        buff.append(")");
    }

}
