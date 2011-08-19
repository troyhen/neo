package org.dia.lex;

import org.dia.Named;
import org.dia.Plugin;

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

//    @Override
//    public boolean isIgnored() { return name.startsWith("!"); }

    public Token consume(int chars) {
        return plugin.consume(this, chars);
    }
}
