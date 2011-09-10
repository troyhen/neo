package org.neo.lex;

import org.neo.Compiler;
import org.neo.Plugin;

/**
 *
 * @author Troy Hheninger
 */
public class LexerString extends LexerBase {

    public final String string;

    public LexerString(Plugin plugin, String name, String string) {
        this(plugin, name, string, false);
    }

    public LexerString(Plugin plugin, String name, String string, boolean ignore) {
        super(plugin, name);
        this.string = string;
    }

    @Override
    public Token nextToken() {
        final int len = string.length();
        if (Compiler.chars(len).toString().equals(string)) {
            return consume(len);
        }
        return null;
    }
}
