package org.dia.core;

import org.dia.Compiler;
import org.dia.Plugin;
import org.dia.lex.Token;

/**
 *
 * @author Troy Heninger
 */
public class Compilation extends Plugin {
    public static final String EOF = "eof";

    @Override
    public void open() {
        addParser("root", "statements");
        addParser("statements", "!terminator* statement*");
        addParser("terminator", "semi");
        addParser("terminator", "eol");
        addParser("terminator", "eof");
//        addParser("statement", "callWithBlock");
//        addParser("statement", "alias");
//        addParser("statement", "statementIf");
//        addParser("statement", "statementUnless");
//        addParser("statement", "statementWhile");
//        addParser("statement", "statementUntil");
        addParser("statement", "expression !terminator+");
//        addParser("statementIf", "statement if expression");
//        addParser("statementUnless", "statement unless expression");
//        addParser("statementWhile", "statement while expression");
//        addParser("statementUntil", "statement until expression");
//        addParser("callWithBlock", "call");
        Compiler.compiler().literals.add(EOF);
    }

//    public static Node call(Node node) {
//        Node identifier = identifier(node);
//        try {
//
//        } catch (Missing e) {}
//    }
//
//    public static Node callWithBlock(Node node) {
//        Node call = node;
//        Node next = call(node);
//        try {
//            Node blockStart = next;
//            Node blockArgList = blockStart(blockStart);
//            next = blockArgs(blockArgList);
//        } catch (Missing e) {}
//        Node statements = next;
//        Node blockEnd = statements(next);
//        next = blockEnd(blockEnd);
//        return next;
//    }
//
//    public static Node statement(Node node) {
//        Node next;
//        try {
//            Node call = callWithBlock(node);
//        } catch (Missing e) {}
//        Node expression = expression(node);
//        next = expression.getNext();
//        return nex;
//    }
//
//    public static Node statements(Node node) {
//        Node last = null;
//        try {
//            for (;;) {
//                node = any(terminator(node));
//                node = statement(node);
//                last = node;
//            }
//        } catch (Missing e) {
//        }
//        return last.getNext();
//    }
//
//    public static Node terminator(Node node) {
//
//    }

//    @Override
//    public Node parse(Node node) {
//        statements(node);
//    }
//
//    @Override
//    public void compile(Node node, Backend back) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }

    @Override
    public Token nextToken() {
        if (Compiler.buffer().length() == 0) {
            return consume(EOF, 0);
        }
        return null;
    }

}
