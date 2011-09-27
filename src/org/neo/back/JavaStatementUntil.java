package org.neo.back;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaStatementUntil implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output();
        boolean needBoolean = !"boolean".equals(node.get(1).getTypeName());
        buff.tab().append("while (!");
        if (needBoolean) buff.append("toboolean(");
        else buff.append("(");
        node.get(1).render("java");
        buff.append(")) {").eol().tabMore();
        node.getFirst().render("java");
        buff.tabLess().tab().append("}").eol();
    }

}
