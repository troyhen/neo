package org.neo.lex;

import org.neo.Plugin;
import org.neo.parse.Engine;

/**
 *
 * @author Troy Heninger
 */
public class LexerEof extends LexerBase {

    public static final String BOF = "terminator_bof";
    public static final String EOF = "terminator_eof";
    
    public LexerEof(Plugin plugin) {
        super(plugin, EOF);
        Engine.engine().setNextToken(new Token(plugin, BOF/*, Engine.engine().nextTokenIndex()*/, "", 1));
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
