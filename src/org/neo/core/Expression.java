package org.neo.core;

/**
 *
 * @author Troy Heninger
 */
public class Expression extends PluginCore {
    
    @Override
    public void open() {
        addParser("expression", "number");
        addParser("expression", "string");
        addParser("expression", "@expression ^operator.as (symbol operator.dot)* symbol");
        addParser("expression", "@expression ^operator.pow @expression");
        addParser("expression", "@expression ^operator.mul @expression");
        addParser("expression", "@expression ^operator.add @expression");
        addParser("expression", "access");
        addParser("access.dot", "@expression ^operator.dot access");
        addParser("access.call", "symbol !start.paren (@expression (!comma? @expression)*)? !end.paren");
        addParser("access.call", "symbol (@expression (!comma? @expression)*)?");
        addParser("expression", "symbol.if @expression !symbol.then statement elseClause? !symbol.end?");
        addParser("expression", "symbol.if @expression !symbol.then? !terminator @block elseClause?");
        addParser("expression", "symbol.unless @expression !symbol.then statement elseClause?");
        addParser("expression", "symbol.unless @expression !symbol.then? !terminator @block elseClause? !symbol.end?");
        addParser("expression", "symbol.while @expression !symbol.do statement elseClause?");
        addParser("expression", "symbol.while @expression !symbol.do? !terminator @block elseClause? !symbol.end");
        addParser("expression", "symbol.until @expression !symbol.do statement elseClause?");
        addParser("expression", "symbol.until @expression !symbol.do? !terminator @block elseClause? !symbol.end");
        addParser("elseClause", "!symbol.else statement");
        addParser("elseClause", "!symbol.else !terminator @block");
    }

}
