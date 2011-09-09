package org.neo.back;

import org.neo.Node;
import org.neo.back.JavaCompilation.Segment;

/**
 *
 * @author Troy Heninger
 */
public class JavaStatementValDeclare implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output(Segment.inside);
        buff.tab();
        Node node0 = node.get(0);
        Node node1 = node.get(1);
        Node node2 = node.get(2);
        buff.append("final ");
        if (node1 != null && node1.getName().equals("operator.as")) {
            node1 = node2;
            while (node1 != null && !node1.getName().startsWith("expression")) {
                buff.append(node1.getText());
                node1 = node1.getNext();
            }
            buff.append(" ").append(node0.getText());
        }
        buff.append(";").eol();
    }

}
