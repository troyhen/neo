package org.rio.lex;

import org.rio.Compiler;
import org.rio.Plugin;

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
        CharSequence buffer = Compiler.buffer();
        if (buffer.length() >= len && buffer.subSequence(0, len).equals(string)) {
            return consume(len);
        }
        return null;
    }
}
