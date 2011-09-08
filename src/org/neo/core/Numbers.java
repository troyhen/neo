package org.neo.core;

import org.neo.Compiler;
import org.neo.lex.LexerPattern;
import org.neo.lex.Token;

/**
 *
 * @author Troy Heninger
 */
public class Numbers extends PluginCore {

    public static final String INTEGER = "number.integer";
    public static final String REAL = "number.real";
    public static final String REAL_FORCED = "number.real.forced";

    @Override
    public Token consume(String name, int chars) {
        String text = Compiler.buffer().subSequence(0, chars).toString();
        Number value;
        if (name.equals(INTEGER)) {
            value = new Integer(text);
        } else {
//Not needed since Java will ignore the trailing 'd'
//            if (name == REAL_FORCED) text = text.substring(0, text.length() - 1);
            value = new Double(text);
        }
        return super.consume(name, chars, value);
    }

    @Override
    public void open() {
        super.open();
        names.add("number");
        add(new LexerPattern(this, REAL, "-?[0-9_]+\\.[0-9_]([eE]-?[0-9_]+)?[dD]?"));
        add(new LexerPattern(this, REAL_FORCED, "-?[0-9_]+[dD]"));
        add(new LexerPattern(this, INTEGER, "-?[0-9_]+"));
    }

}