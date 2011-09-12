package org.neo.back;

import org.neo.Node;
import org.neo.back.JavaCompilation.Segment;

/**
 *
 * @author Troy Heninger
 */
public class JavaReference implements Backend {

    @Override
    public void render(Node node) {
        //CodeBuilder buff = JavaCompilation.output(Segment.inside);
        node.getFirst().render("java");
    }

}
