package org.neo.back;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaStatementValAssign implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output();
        buff.tab();
        Node node0 = node.get(0);
        Node node1 = node.get(1);
        buff.append("final ");
        buff.append(node1.getType()).append(" ");
        if (node1.getName().equals("operator_as")) {
            node1 = node1.getNext();
        }
        buff.append(node0.getText()).append(" = ");
        node1.getFirst().render("java");
        buff.append(";").eol();
    }

}
