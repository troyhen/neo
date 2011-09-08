package org.neo.core;

/**
 *
 * @author Troy Heninger
 */
public class Compilation extends PluginCore {

    @Override
    public void open() {
//        add(new LexerEof(this));
//        addParser("statement", "callWithBlock");
//        addParser("statement", "alias");

        addParser("statement.if", "statement !symbol.if @expression");
        addParser("statement.unless", "statement !symbol.unless @expression");
        addParser("statement.while", "statement !symbol.while @expression");
        addParser("statement.until", "statement !symbol.until @expression");
        addParser("statement", "@expression");
//        addParser("callWithBlock", "call");
        addParser("statements", "!terminator* (statement !terminator+)+");
    }

}
