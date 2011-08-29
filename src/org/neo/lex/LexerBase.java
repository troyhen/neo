package org.neo.lex;

import org.neo.Named;
import org.neo.Plugin;

/**
 *
 * @author Troy Heninger
 */
public abstract class LexerBase implements Lexer, Named {

    public final Plugin plugin;
    public final String name;

    public LexerBase(Plugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    @Override
    public String getName() { return /*isIgnored() ? name.substring(1) :*/ name; }

    @Override
    public Plugin getPlugin() { return plugin; }

    public Token consume(int chars) {
        return plugin.consume(this, chars);
    }

    public Token consume(int chars, Object value) {
        return plugin.consume(this, chars, value);
    }
}
