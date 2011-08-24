package org.rio.core;

import org.rio.Plugin;
import org.rio.lex.LexerPattern;

/**
 *
 * @author Troy Heninger
 */
public class Symbol extends Plugin {

    public static final String SYMBOL = "symbol";
    public static final String SYMBOL_QUOTED = "symbol.quoted";
    
    @Override
    public void open() {
        super.open();
        names.add("string");
        add(new LexerPattern(this, SYMBOL, "[A-Za-z_$][A-Za-z0-9_$]*\\??"));
        add(new LexerPattern(this, SYMBOL_QUOTED, "`([^`\\r\\n])`", 1));
    }

}
