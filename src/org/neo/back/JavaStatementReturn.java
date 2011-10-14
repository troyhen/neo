package org.neo.back;

import org.neo.parse.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaStatementReturn implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output();
        buff.tab().append("return");
        if (node.get(0) != null) {
            buff.append(" ");
            node.get(0).render("java");
        }
        buff.append(";").eol();
    }

}
