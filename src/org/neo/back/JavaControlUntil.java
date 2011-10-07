package org.neo.back;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaControlUntil extends JavaControlWhile {

    @Override
    protected void renderCondition(Node node, CodeBuilder buff, boolean needBoolean) {
        JavaStatementUnless.renderConditionLeg(getConditionLeg(node), buff, needBoolean);
    }

}
