package org.neo.lex;

import org.neo.Compiler;
import org.neo.Plugin;

/**
 *
 * @author Troy Heninger
 */
public class LexerEof extends LexerBase {

    public static final String EOF = "eof";

    public LexerEof(Plugin plugin) {
        super(plugin, EOF);
    }

    @Override
    public Token nextToken() {
        if (Compiler.buffer().length() == 0) {
            return consume(0);
        }
        return null;
    }

}
