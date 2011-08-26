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
        addParser("expression", "@expression ^operator.pow @expression");
        addParser("expression", "@expression ^operator.mul @expression");
        addParser("expression", "@expression ^operator.add @expression");
    }

    public Node expression(Node first, Node after) {
        return after;
    }
}
