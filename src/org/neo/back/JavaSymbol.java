package org.neo.back;

import org.neo.Node;
import org.neo.back.JavaCompilation.Segment;

/**
 *
 * @author Troy Heninger
 */
public class JavaSymbol implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output(Segment.inside);
        buff.append(node.getValue());
    }

}
