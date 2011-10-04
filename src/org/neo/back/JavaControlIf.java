package org.neo.back;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaControlIf implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output();
        boolean needBoolean = !"boolean".equals(node.getFirst().getTypeName());
        buff.append("if (");
        if (needBoolean) buff.append("toboolean(");
        node.getFirst().render("java");
        if (needBoolean) buff.append(")");
        buff.append(") {").eol().tabMore();
        node.get(1).render("java");
        buff.tabLess().tab().append("}");
        if (node.get(2) != null) {
            node.get(2).render("java");
        } else {
            buff.eol();
        }
    }

    public void renderExpression(Node node, Node expression) {
        CodeBuilder buff = JavaCompilation.output();
        boolean needBoolean = !"boolean".equals(node.getFirst().getTypeName());
        buff.append("if (");
        if (needBoolean) buff.append("toboolean(");
        node.getFirst().render("java");
        if (needBoolean) buff.append(")");
        buff.append(") {").eol().tabMore();
        node.get(1).render("java");
        buff.tabLess().tab().append("}");
        if (node.get(2) != null) {
            node.get(2).render("java");
        } else {
            buff.eol();
        }
    }

}
