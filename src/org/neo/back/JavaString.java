package org.neo.back;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaString implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output();
        buff.append("\"");
        buff.append(node.getValue());   // TODO escape double quotes
        buff.append("\"");
    }

}
