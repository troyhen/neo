package org.dia.core;

import org.dia.Node;
import org.dia.Plugin;

/**
 *
 * @author Troy Heninger
 */
public class Expression extends Plugin {
    
    @Override
    public void open() {
        addParser("expression", "number", "number");
        addParser("expression", "expression ^operator.pow expression", "operator(expression[0] expression[1])");
        addParser("expression", "expression ^operator.mul expression", "operator(expression[0] expression[1])");
        addParser("expression", "expression ^operator.add expression", "operator(expression[0] expression[1])");
    }

    public Node expression(Node first, Node after) {
        return after;
    }
}
