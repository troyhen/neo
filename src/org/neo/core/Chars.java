package org.neo.core;

import org.neo.lex.LexerPattern;
import org.neo.lex.Token;

/**
 */
public class Chars extends CorePlugin {

    @Override
    public Token consume(String name, int chars, Object value, String type) {
        return super.consume(name, chars, decode((CharSequence) value), "char");
    }

    private Object decode(CharSequence value) {
        String string = Strings.decode(value);
        return string.charAt(0);
    }

    @Override
    public void open() {
        super.open();
        names.add("character");
        add(new LexerPattern(this, "character", "\\?(\\\\u[0-9a-fA-F]{1,4}|\\\\x[0-9a-fA-F]{1,2}|\\\\[0-7]{1,3}|\\S)", 1));
        addParser("expression0", "character");
    }
}
