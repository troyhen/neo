package org.neo.core;

import org.neo.Compiler;
import org.neo.Named;
import org.neo.Plugin;
import org.neo.PluginBase;
import org.neo.lex.LexerPattern;
import org.neo.lex.Token;

/**
 *
 * @author Troy Heninger
 */
public class Symbol extends PluginBase {

    public static final String SYMBOL = "symbol";
//    public static final String SYMBOL_QUOTED = "symbol.quoted";

    private LexerPattern symbol;

    @Override
    public Token consume(Named named, int chars) {
        final String text = Compiler.buffer().subSequence(0, chars).toString();
        if (named == symbol) {
            return super.consume(new Named() {

                @Override
                public String getName() {
                    return SYMBOL + '.' + text;
                }

                @Override
                public Plugin getPlugin() {
                    return Symbol.this;
                }
            }, chars);
        }
        return super.consume(this, chars);
    }
    
    @Override
    public void open() {
        super.open();
        names.add(SYMBOL);
        add(symbol = new LexerPattern(this, SYMBOL, "[A-Za-z_$][A-Za-z0-9_$]*\\??"));
//        add(new LexerPattern(this, SYMBOL_QUOTED, "`([^`\\r\\n])`", 1));
    }

}
