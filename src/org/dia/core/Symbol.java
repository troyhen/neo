package org.dia.core;

import org.dia.Compiler;
import org.dia.Node;
import org.dia.Plugin;
import org.dia.back.Backend;
import org.dia.lex.LexerPattern;

/**
 *
 * @author Troy Heninger
 */
public class Symbol extends Plugin {

    public static final String SYMBOL = "symbol";
    public static final String SYMBOL_QUOTED = "symbol.quoted";
    
    @Override
    public void open() {
        super.open();
        names.add("string");
        add(new LexerPattern(this, SYMBOL, "[A-Za-z_$][A-Za-z0-9_$]*\\??"));
        add(new LexerPattern(this, SYMBOL_QUOTED, "`([^`\\r\\n])`", 1));
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
