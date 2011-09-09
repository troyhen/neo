package org.neo.core;

import org.neo.Compiler;
import org.neo.lex.LexerPattern;
import org.neo.lex.Token;

/**
 *
 * @author Troy Heninger
 */
public class RegEx extends CorePlugin {

    public static final String NAME = "regex";
    public static final String ABREV = "r";

    @Override
    public void open() {
        super.open();
        names.add(NAME);
        names.add(ABREV);
        add(new LexerPattern(this, NAME, "/((\\[(\\\\\\]|[^\\]])+|\\/|[^/])+)/[iop]?", 1));
    }

    @Override
    public Token consume(String name, int chars) {
        Token token = super.consume(name, chars);
        token.setType("java.util.regex.Pattern");
        return token;
    }

}
