package org.neo.back;

import org.neo.Node;
import org.neo.NeoException;

/**
 *
 * @author Troy Heninger
 */
public class JavaExpression implements Render {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output();
        if (node.getName().equals("operator.as")) {
            buff.append("to");
            if (node.get(2) == null) {
                buff.append(node.get(1).getValue());
                buff.append("(");
                render(node.get(0));
                buff.append(")");
            } else {
                buff.append("(");
                render(node.get(0));
                buff.append(", (");
                JavaImport imp = new JavaImport();
                buff.append(imp.getPath(node.get(1)));
                buff.append(") null)");
            }
        } else if(node.getName().startsWith("operator")) {
            buff.append("(");
            render(node.get(0));
            buff.append(" ");
            buff.append(node.getValue());
            buff.append(" ");
            render(node.get(1));
            buff.append(")");
        } else if (node.getName().startsWith("symbol")) {
            buff.append(node.getValue());
        } else if (node.getName().startsWith("string")) {
            buff.append("\"");
            buff.append(node.getValue());   // TODO escape double quotes
            buff.append("\"");
        } else if (node.getName().startsWith("number")) {
            buff.append(node.getValue());
//        } else if (node.getName().startsWith("access.dot")) {
//            buff.append("(");
        } else if (node.getName().startsWith("access.call")) {
            node = node.getFirst();
            render(node);
            node = node.getNext();
            buff.append("(");
            while (node != null) {
                render(node);
                node = node.getNext();
            }
            buff.append(")");
        } else {
            throw new NeoException("Unexpected node: " + node);
        }
    }

 }
