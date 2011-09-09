package org.neo.back;

import org.neo.Node;
import org.neo.back.JavaCompilation.Segment;

/**
 *
 * @author Troy Heninger
 */
public class JavaStatementUntil implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output(Segment.inside);
        buff.tab().append("while (!");
        node.get(1).render("java");
        buff.append(") {").eol().tabMore();
        node.getFirst().render("java");
        buff.tabLess().tab().append("}").eol();
    }

}
