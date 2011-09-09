package org.neo.back;

import org.neo.Node;
import org.neo.back.JavaCompilation.Segment;

/**
 *
 * @author Troy Heninger
 */
public class JavaStatementVarAssign implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output(Segment.inside);
        buff.tab();
        Node node0 = node.get(0);
        Node node1 = node.get(1);
        if (node1 != null && node1.getName().equals("operator.as")) {
            node1 = node.get(2);
            while (node1 != null && !node1.getName().startsWith("expression")) {
                buff.append(node1.getText());
                node1 = node1.getNext();
            }
            buff.append(" ");
        } else {
            buff.append(node1.getFirst().getType()).append(" ");
        }
        buff.append(node0.getText()).append(" = ");
        node1.getFirst().render("java");
        buff.append(";").eol();
    }

}
