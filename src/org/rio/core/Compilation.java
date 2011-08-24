package org.rio.core;

import org.rio.Plugin;
import org.rio.lex.LexerEof;

/**
 *
 * @author Troy Heninger
 */
public class Compilation extends Plugin {

    @Override
    public void open() {
        add(new LexerEof(this));
        addParser("statements", "!terminator* (@statement !terminator)+");
        addParser("terminator", "semi");
        addParser("terminator", "eol");
        addParser("terminator", "eof");
//        addParser("statement", "callWithBlock");
//        addParser("statement", "alias");
//        addParser("statement", "statementIf");
//        addParser("statement", "statementUnless");
//        addParser("statement", "statementWhile");
//        addParser("statement", "statementUntil");
        addParser("statement", "@expression [terminator | eof]");
//        addParser("statementIf", "statement if expression");
//        addParser("statementUnless", "statement unless expression");
//        addParser("statementWhile", "statement while expression");
//        addParser("statementUntil", "statement until expression");
//        addParser("callWithBlock", "call");
//        Compiler.compiler().literals.add(EOF);
    }

}
