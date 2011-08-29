package org.neo.core;

import org.neo.PluginBase;
import org.neo.lex.LexerEof;

/**
 *
 * @author Troy Heninger
 */
public class Compilation extends PluginBase {

    @Override
    public void open() {
        add(new LexerEof(this));
        addParser("terminator", "semi");
        addParser("terminator", "eol");
        addParser("terminator", "eof");
//        addParser("statement", "callWithBlock");
//        addParser("statement", "alias");
//        addParser("statement", "statementIf");
//        addParser("statement", "statementUnless");
//        addParser("statement", "statementWhile");
//        addParser("statement", "statementUntil");
        addParser("statement", "importStatement !terminator+");
        addParser("statement", "@expression !terminator+");
//        addParser("statementIf", "statement if expression");
//        addParser("statementUnless", "statement unless expression");
//        addParser("statementWhile", "statement while expression");
//        addParser("statementUntil", "statement until expression");
//        addParser("callWithBlock", "call");
//        Compiler.compiler().literals.add(EOF);
        addParser("statements", "!terminator* statement+");
    }

}
