package org.neo.core;

import org.neo.Compiler;
import org.neo.PluginBase;
import org.neo.lex.LexerPattern;
import org.neo.lex.Token;

/**
 *
 * @author Troy Heninger
 */
public class Symbol extends PluginBase {

    public static final String SYMBOL = "symbol";

    @Override
    public Token consume(String name, int chars) {
        final String text = Compiler.buffer().subSequence(0, chars).toString();
        if (name.equals(SYMBOL)) {
            return super.consume(SYMBOL + '.' + text, chars);
        }
        return super.consume(name, chars);
    }
    
    @Override
    public void open() {
        super.open();
        names.add(SYMBOL);
        add(new LexerPattern(this, SYMBOL, "[A-Za-z_$][A-Za-z0-9_$]*\\??"));
    }

}
