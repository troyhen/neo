package org.neo.back;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaCallDot implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output();
        Node expression = node.getFirst();
        Node symbol = expression.getNext();
        Node param = symbol.getNext();
        expression.render("java");
        buff.append(".");
        symbol.render("java");
        buff.append("(");
        String comma = "";
        while (param != null) {
            buff.append(comma);
            param.render("java");
            param = param.getNext();
        }
        buff.append(")");
    }

}
