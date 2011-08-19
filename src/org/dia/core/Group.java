package org.dia.core;

import org.dia.Compiler;
import org.dia.Node;
import org.dia.Plugin;
import org.dia.back.Backend;
import org.dia.lex.LexerChar;

/**
 *
 * @author Troy Heninger
 */
public class Group extends Plugin {

    public static final String NAME = "group";

    @Override
    public void open() {
        super.open();
        names.add(NAME);
        add(new LexerChar(this, "start.brace", '{'));
        add(new LexerChar(this, "end.brace", '}'));
        add(new LexerChar(this, "start.bracket", '['));
        add(new LexerChar(this, "end.bracket", ']'));
        add(new LexerChar(this, "start.paren", '('));
        add(new LexerChar(this, "end.paren", ')'));
    }

//    @Override
//    public Node parse(Node node) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void compile(Node node, Backend back) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }

}
