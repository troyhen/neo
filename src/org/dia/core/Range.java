package org.dia.core;

import org.dia.Compiler;
import org.dia.Node;
import org.dia.Plugin;
import org.dia.back.Backend;
import org.dia.lex.LexerString;

/**
 *
 * @author Troy Heninger
 */
public class Range extends Plugin {

    public static final String NAME = "range";
    public static final String RANGE_INCLUSIVE = "range.inclusive";
    public static final String RANGE_EXCLUSIVE = "range.exclusive";
    
    @Override
    public void open() {
        super.open();
        names.add(NAME);
        add(new LexerString(this, RANGE_INCLUSIVE, "..."));
        add(new LexerString(this, RANGE_EXCLUSIVE, ".."));
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
