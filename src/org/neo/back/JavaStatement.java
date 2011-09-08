package org.neo.back;

import org.neo.Node;
import org.neo.back.JavaCompilation.Segment;

/**
 *
 * @author Troy Heninger
 */
public class JavaStatement implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output(Segment.inside);
        buff.tab();
        node = node.getFirst();
        node.render("java");
        buff.append(";").eol();
    }

}
