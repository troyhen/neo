package org.neo.back;

import org.neo.parse.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaSymbol implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output();
        buff.append(node.getValue());
    }

}
