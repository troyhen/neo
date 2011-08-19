package org.dia.lex;

import org.dia.Plugin;

/**
 *
 * @author Troy Heninger
 */
public abstract class LexerTool implements Lexer {

    public final Plugin plugin;
    public final String name;

    public LexerTool(Plugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public Token consume(int chars) {
        return plugin.consume(name, chars);
    }
    
    @Override
    public String getName() { return isIgnored() ? name.substring(1) : name; }

    @Override
    public boolean isIgnored() { return name.startsWith("!"); }
}
