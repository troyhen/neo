package org.neo.core;

import org.neo.Compiler;
import org.neo.lex.LexerPattern;
import org.neo.lex.Token;

/**
 *
 * @author Troy Heninger
 */
public class Symbol extends CorePlugin {

    public static final String SYMBOL = "symbol";

    @Override
    public Token consume(String name, int chars, Object value, String text) {
        text = Compiler.buffer().subSequence(0, chars).toString();
        return super.consume(name + '.' + text, chars, value, text);
    }
    
    @Override
    public void open() {
        super.open();
        names.add(SYMBOL);
        add(new LexerPattern(this, "keyword", "(def|do|else|if|then|unless|until|return|var|val|while)"));
        add(new LexerPattern(this, SYMBOL, "[A-Za-z_$][A-Za-z0-9_$]*\\??"));
    }

}
