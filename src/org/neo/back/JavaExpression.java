package org.neo.back;

import org.neo.Node;
import org.neo.RioException;

/**
 *
 * @author Troy Heninger
 */
public class JavaExpression implements Render {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output();
        if (node.getName().startsWith("operator")) {
            buff.append("(");
            render(node.get(0));
            buff.append(" ");
            buff.append(node.getValue());
            buff.append(" ");
            render(node.get(1));
            buff.append(")");
        } else if (node.getName().startsWith("number")) {
            buff.append(node.getValue());
        } else {
            throw new RioException("Unexpected node: " + node);
        }
    }

 }
