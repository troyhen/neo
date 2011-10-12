package org.neo.core;

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
    public Token consume(String name, int chars, Object value, String type) {
        return super.consume(name, chars, value, "java.util.regex.Pattern");
    }

    @Override
    public void open() {
        super.open();
        names.add(NAME);
        names.add(ABREV);
        add(new LexerPattern(this, NAME, "/((\\[(\\\\\\]|[^\\]])+|\\/|[^/])+)/[iop]?", 1));
        addParser("expression", "regex");
    }

}
