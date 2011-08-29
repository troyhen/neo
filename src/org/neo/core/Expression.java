package org.neo.core;

import org.neo.Node;
import org.neo.PluginBase;

/**
 *
 * @author Troy Heninger
 */
public class Expression extends PluginBase {
    
    @Override
    public void open() {
        addParser("expression", "number");
        addParser("expression", "string");
        addParser("expression", "@expression ^operator.pow @expression");
        addParser("expression", "@expression ^operator.mul @expression");
        addParser("expression", "@expression ^operator.add @expression");
        addParser("expression", "access");
        addParser("access.dot", "@expression ^operator.dot access");
        addParser("access.call", "symbol !start.paren (@expression (!comma @expression)*)? !end.paren");
        addParser("access.call", "symbol (@expression (!comma @expression)*)?");
    }

    public Node expression(Node first, Node after) {
        return after;
    }
}
