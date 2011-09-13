package org.neo.core;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class Variable extends CorePlugin {
    
    @Override
    public void open() {
        addParser("cast", "^operator.as (symbol operator.dot)* symbol (start.bracket end.bracket)*");
        addParser("statement.varDeclare",
                "!keyword.var symbol @cast?");// (start.bracket @expression end.bracket)?");
        addParser("statement.varAssign",
                "@statement.varDeclare !operator.eq expression");
        addParser("statement.valDeclare",
                "!keyword.val symbol @cast?");// (start.bracket @expression end.bracket)?");
        addParser("statement.valAssign",
                "@statement.valDeclare !operator.eq expression");
    }

    @Override
    public Node transform(Node node) {
        if (node.getName().equals("cast")) {    // see expression: @expression ^cast
            /*
             * Convert:
             * cast
             *     @expression
             *     operator.as
             *         symbol
             *         ...
             * into:
             * operator.as
             *     @expression
             */
            Node operatorAs = node.get(1);
            while (operatorAs.getFirst() != null) {
                operatorAs.getFirst().unlink();
            }
            operatorAs.addFirst(node.getFirst());
            node.insertAfter(operatorAs);
            node.unlink();
            return operatorAs;
        }
        return super.transform(node);
    }

}
