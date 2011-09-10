package org.neo.lex;

import org.neo.Compiler;
import org.neo.Plugin;

/**
 *
 * @author Troy Heninger
 */
public abstract class LexerBase implements Lexer {

    public final Plugin plugin;
    public final String name;

    public LexerBase(Plugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    @Override
    public String getName() { return name; }

    @Override
    public Plugin getPlugin() { return plugin; }

    public Token consume(int chars) {
        return consume(chars, null);
    }

    public Token consume(int chars, Object value) {
        return plugin.consume(name, chars, value, null);
    }
}
