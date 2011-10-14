package org.neo.lex;

import org.neo.Plugin;
import org.neo.parse.Engine;

/**
 *
 * @author Troy Heninger
 */
public class LexerEof extends LexerBase {

    public static final String EOF = "eof";

    public LexerEof(Plugin plugin) {
        super(plugin, EOF);
    }

    public boolean atEof() {
        return Engine.buffer().length() == 0;
    }

    @Override
    public Token nextToken() {
        if (atEof()) {
            return consume(0);
        }
        return null;
    }

}
