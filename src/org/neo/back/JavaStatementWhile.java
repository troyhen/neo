package org.neo.back;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaStatementWhile implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output();
        boolean needBoolean = !"boolean".equals(node.get(1).getTypeName());
        buff.tab().append("while (");
        if (needBoolean) buff.append("toboolean(");
        node.get(1).render("java");
        if (needBoolean) buff.append(")");
        buff.append(") {").eol().tabMore();
        node.getFirst().render("java");
        buff.tabLess().tab().append("}").eol();
    }

}
