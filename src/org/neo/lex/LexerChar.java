package org.neo.lex;

import org.neo.Compiler;
import org.neo.Plugin;

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
        CharSequence buffer = Compiler.buffer();
        if (buffer.length() > 0 && buffer.charAt(0) == ch) {
            return consume(1);
        }
        return null;
    }

}