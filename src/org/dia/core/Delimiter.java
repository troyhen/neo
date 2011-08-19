package org.dia.core;

import org.dia.Node;
import org.dia.Compiler;
import org.dia.Plugin;
import org.dia.back.Backend;
import org.dia.lex.LexerChar;

/**
 *
 * @author Troy Heninger
 */
public class Delimiter extends Plugin {

    public static final String COMMA = "comma";
    public static final String SEMI = "semi";

    @Override
    public void open() {
        super.open();
        names.add("delimiter");
        add(new LexerChar(this, COMMA, ','));
        add(new LexerChar(this, SEMI, ';'));
    }

//    @Override
//    public void parse(Compiler parser) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void compile(Node node, Backend back) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }

}
