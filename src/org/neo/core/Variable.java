package org.neo.core;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class Variable extends CorePlugin {
    
    @Override
    public void open() {
        addParser("statement.varDeclare",
                "!keyword.var symbol (operator.as (symbol operator.dot)* symbol)?");
        addParser("statement.varAssign",
                "@statement.varDeclare !operator.eq expression");
        addParser("statement.valDeclare",
                "!keyword.val symbol (operator.as (symbol operator.dot)* symbol)?");
        addParser("statement.valAssign",
                "@statement.valDeclare !operator.eq expression");
    }

    @Override
    public Node transform(Node node) {
//        if (no)
        return super.transform(node);
    }

}
