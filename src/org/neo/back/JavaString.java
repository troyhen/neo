package org.neo.back;

import org.neo.Node;
import org.neo.core.Strings;

/**
 *
 * @author Troy Heninger
 */
public class JavaString implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output();
        if (node.getFirst() != null) {
            node = node.getFirst();
            String add = "";
            while (node != null) {
                buff.append(add);
                add = " + ";
                if (!node.isNamed("string")) {
                    buff.append("(");
                    node.render("java");
                    buff.append(")");
                } else node.render("java");
                node = node.getNext();
            }
        } else {
            buff.append("\"");
            buff.append(Strings.encode((CharSequence) node.getValue()));
            buff.append("\"");
        }
    }

}
