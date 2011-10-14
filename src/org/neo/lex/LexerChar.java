package org.neo.lex;

import org.neo.Plugin;
import org.neo.parse.Engine;

/**
 *
 * @author Troy Heninger
 */
public class LexerChar extends LexerBase {

    private char ch;

    public LexerChar(Plugin plugin, String name, char c) {
        super(plugin, name);
        ch = c;
    }

    @Override
    public Token nextToken() {
        CharSequence buffer = Engine.buffer();
        if (buffer.length() > 0 && buffer.charAt(0) == ch) {
            return consume(1);
        }
        return null;
    }

}
