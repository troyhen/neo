package org.neo.core;

import org.neo.Compiler;
import org.neo.lex.LexerPattern;
import org.neo.lex.LexerString;
import org.neo.lex.Token;

/**
 *
 * @author Troy Heninger
 */
public class Numbers extends CorePlugin {

    public static final String TRUE = "number.true";
    public static final String FALSE = "number.false";
    public static final String INTEGER = "number.integer";
    public static final String REAL = "number.real";
    public static final String REAL_FORCED = "number.real.forced";

    @Override
    public Token consume(String name, int chars) {
        String text = Compiler.buffer().subSequence(0, chars).toString();
        Object value;
        String type;
        if (name.equals(TRUE) || name.equals(FALSE)) {
            value = text;
            type = "boolean";
        } else if (name.equals(INTEGER)) {
            value = new Integer(text);
            type = "int";
        } else {
                // Note, Java will ignore the trailing 'f'
            if (text.toLowerCase().endsWith("f")) {
                value = new Float(text);
                type = "float";
            } else {
                value = new Double(text);
                type = "double";
            }
        }
        return super.consume(name, chars, value, type);
    }

    @Override
    public void open() {
        super.open();
        names.add("number");
        add(new LexerString(this, TRUE, "true"));
        add(new LexerString(this, FALSE, "false"));
        add(new LexerPattern(this, REAL, "-?[0-9_]+\\.[0-9_]([eE]-?[0-9_]+)?[fF]?"));
        add(new LexerPattern(this, REAL, "-?[0-9_]+[fF]"));
        add(new LexerPattern(this, INTEGER, "-?[0-9_]+"));
    }

}