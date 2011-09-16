package org.neo.back;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaStatementVarDeclare implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output();
        buff.tab();
        Node node0 = node.get(0);
        Node node1 = node.get(1);
        if (node1 != null && node1.getName().equals("operator_as")) {
            buff.append(node1.getType()).append(" ").append(node0.getText());
        }
        buff.append(";").eol();
    }

}
