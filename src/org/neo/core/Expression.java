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
        addParser("expression", "@expression ^operator.as (symbol operator.dot)* symbol");
        addParser("expression", "@expression ^operator.pow @expression");
        addParser("expression", "@expression ^operator.mul @expression");
        addParser("expression", "@expression ^operator.add @expression");
        addParser("expression", "access");
        addParser("expression", "ifExpression");
        addParser("expression", "unlessExpression");
        addParser("expression", "whileExpression");
        addParser("expression", "untilExpression");
        addParser("access.dot", "@expression ^operator.dot access");
        addParser("access.call", "symbol !start.paren (@expression (!comma @expression)*)? !end.paren");
        addParser("access.call", "symbol (@expression (!comma @expression)*)?");
        addParser("elseClause", "!symbol.else statement");
        addParser("elseClause", "!symbol.else !terminator statements");
        addParser("ifExpression", "!symbol.if expression !symbol.then statement elseClause?");
        addParser("ifExpression", "!symbol.if expression !symbol.then terminator statements elseClause? !symbol.end");
        addParser("unlessExpression", "!symbol.unless expression !symbol.then statement elseClause?");
        addParser("unlessExpression", "!symbol.unless expression !symbol.then terminator statements elseClause? !symbol.end");
        addParser("whileExpression", "!symbol.while expression !symbol.then statement elseClause?");
        addParser("whileExpression", "!symbol.while expression !symbol.then terminator statements elseClause? !symbol.end");
        addParser("untilExpression", "!symbol.until expression !symbol.then statement elseClause?");
        addParser("untilExpression", "!symbol.until expression !symbol.then terminator statements elseClause? !symbol.end");
    }

    public Node expression(Node first, Node after) {
        return after;
    }
}
