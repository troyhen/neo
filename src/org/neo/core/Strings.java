package org.neo.core;

import org.neo.PluginBase;
import org.neo.lex.LexerPattern;

/**
 *
 * @author Troy Heninger
 */
public class Strings extends PluginBase {

    public static final String STRING_DOUBLE = "string.double";
//    public static final String STRING_DOUBLE_BAD = "string.double.bad";
//    public static final String STRING_DOUBLE_BAD2 = "string.double.bad2";
    public static final String STRING_MULTILINE = "string.multiline";
    public static final String STRING_SINGLE = "string.single";
//    public static final String STRING_SINGLE_BAD = "string.single.bad";
//    public static final String STRING_SINGLE_BAD2 = "string.single.bad2";

    @Override
    public void open() {
        super.open();
        names.add("string");
        add(new LexerPattern(this, STRING_SINGLE, "'((\\\\'|[^'\\r\\n])*)'", 1));
//        add(new LexerPattern(this, STRING_SINGLE_BAD, "'((\\\\'|[^'\\r\\n])*)[\\r\\n]", 1));
//        add(new LexerPattern(this, STRING_SINGLE_BAD2, "'((\\\\'|[^'\\r\\n])*)$", 1));
            // multiline must come before double
        add(new LexerPattern(this, STRING_MULTILINE, "\"\"\"((\"\"[^\"]|\"[^\"]|[^\"])*)\"\"\"", 1));
        add(new LexerPattern(this, STRING_DOUBLE, "\"((\\\\\"|[^\"\\r\\n])*)\"", 1));
//        add(new LexerPattern(this, STRING_DOUBLE_BAD, "\"((\\\"|[^\"\\r\\n])*)[\\r\\n]", 1));
    }

}
