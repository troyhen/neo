package org.neo.back;

import org.neo.Node;
import org.neo.back.JavaCompilation.Segment;

/**
 *
 * @author Troy Heninger
 */
public class JavaStatementUnless implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output(Segment.inside);
        boolean needBoolean = !"boolean".equals(node.get(1).getType());
        buff.tab().append("if (!");
        if (needBoolean) buff.append("toboolean(");
        else buff.append("(");
        node.get(1).render("java");
        buff.append(")) {").eol().tabMore();
        node.getFirst().render("java");
        buff.tabLess().tab().append("}").eol();
    }

}
