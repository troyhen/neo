package org.neo.core;

import org.neo.lex.LexerChar;

/**
 *
 * @author Troy Heninger
 */
public class Delimiter extends CorePlugin {

    public static final String COMMA = "comma";
    public static final String SEMI = "terminator_semi";

    @Override
    public void open() {
        super.open();
        names.add("delimiter");
        add(new LexerChar(this, COMMA, ','));
        add(new LexerChar(this, SEMI, ';'));
    }

}
