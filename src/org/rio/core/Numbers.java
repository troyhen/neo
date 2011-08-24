package org.rio.core;

import org.rio.Compiler;
import org.rio.Plugin;
import org.rio.lex.Lexer;
import org.rio.lex.LexerPattern;
import org.rio.lex.Token;

/**
 *
 * @author Troy Heninger
 */
public class Numbers extends Plugin {

    public static final String INTEGER = "number.integer";
    public static final String REAL = "number.real";
    public static final String REAL_FORCED = "number.real.forced";

    @Override
    public Token consume(Lexer lexer, int chars) {
        String text = Compiler.buffer().subSequence(0, chars).toString();
        Number value;
        if (lexer.getName().equals(INTEGER)) {
            value = new Integer(text);
        } else {
//Not needed since Java will ignore the trailing 'd'
//            if (name == REAL_FORCED) text = text.substring(0, text.length() - 1);
            value = new Double(text);
        }
        return super.consume(this, chars, value);
    }

    @Override
    public void open() {
        super.open();
        names.add("number");
        add(new LexerPattern(this, REAL, "-?[0-9_]+\\.[0-9_]([eE]-?[0-9_]+)?[dD]?"));
        add(new LexerPattern(this, REAL_FORCED, "-?[0-9_]+[dD]"));
        add(new LexerPattern(this, INTEGER, "-?[0-9_]+"));
//        addParser("number", INTEGER);
//        addParser("number", REAL);
//        addParser("number", REAL_FORCED);
    }

}