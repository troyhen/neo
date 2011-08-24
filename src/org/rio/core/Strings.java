package org.rio.core;

import org.rio.Plugin;
import org.rio.lex.LexerPattern;

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

}
