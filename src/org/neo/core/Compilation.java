package org.neo.core;

import org.neo.Node;
import org.neo.back.JavaCompilation;
import org.neo.back.JvmCompilation;
//import org.neo.lex.LexerEof;

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

        addParser("statement", "@expression !terminator+");
        addParser("statement.if", "@expression !symbol.if @expression !terminator+");
        addParser("statement.unless", "@expression !symbol.unless @expression !terminator+");
        addParser("statement.while", "@expression !symbol.while @expression !terminator+");
        addParser("statement.until", "@expression !symbol.until @expression !terminator+");
//        addParser("callWithBlock", "call");
        addParser("statements", "!terminator* statement+");
    }

}
