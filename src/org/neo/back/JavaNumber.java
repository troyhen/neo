package org.neo.back;

import org.neo.parse.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaNumber implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output();
        buff.append(node.getValue());
        if (node.getValue() instanceof Float) buff.append("f");
        if (node.getValue() instanceof Long) buff.append("l");
    }

}
