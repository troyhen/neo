package org.rio.core;

import org.rio.Node;
import org.rio.PluginBase;

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
