package org.rio.core;

import org.rio.Plugin;
import org.rio.lex.LexerChar;

/**
 *
 * @author Troy Heninger
 */
public class Delimiter extends Plugin {

    public static final String COMMA = "comma";
    public static final String SEMI = "semi";

    @Override
    public void open() {
        super.open();
        names.add("delimiter");
        add(new LexerChar(this, COMMA, ','));
        add(new LexerChar(this, SEMI, ';'));
    }

}
