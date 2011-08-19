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
        addParser("expression", "number");
        addParser("expression", "expression ^operator.pow expression");
        addParser("expression", "expression ^operator.mul expression");
        addParser("expression", "expression ^operator.add expression");
    }

    public Node expression(Node first, Node after) {
        return after;
    }
}
