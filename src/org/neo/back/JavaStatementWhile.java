package org.neo.back;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaStatementWhile extends JavaStatementIf {

    @Override
    protected void renderHead(CodeBuilder buff) {
        buff.tab().append("while (");
    }

}
