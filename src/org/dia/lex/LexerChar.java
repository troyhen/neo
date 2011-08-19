package org.dia.lex;

import org.dia.Compiler;
import org.dia.Plugin;

/**
 *
 * @author Troy Heninger
 */
public class LexerChar extends LexerTool {

    private char ch;

    public LexerChar(Plugin plugin, String name, char c) {
        super(plugin, name);
        ch = c;
    }

    @Override
    public Token nextToken() {
        CharSequence buffer = Compiler.buffer();
        if (buffer.charAt(0) == ch) {
            return consume(1);
        }
        return null;
    }

}
