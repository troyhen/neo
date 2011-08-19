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
public class Strings extends Plugin {

    public static final String STRING_DOUBLE = "string.double";
    public static final String STRING_MULTILINE = "string.multiline";
    public static final String STRING_SINGLE = "string.single";

    @Override
    public void open() {
        super.open();
        names.add("string");
        add(new LexerPattern(this, STRING_SINGLE, "'((\\'|[^\"\\r\\n])*)'", 1));
        add(new LexerPattern(this, STRING_DOUBLE, "\"((\\\"|[^\"\\r\\n])*)\"", 1));
        add(new LexerPattern(this, STRING_MULTILINE, "\"((\\\"|[^\"\\r\\n])*)\"", 1));
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
