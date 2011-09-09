package org.neo.lex;

import java.util.regex.Pattern;
import org.neo.Node;
import org.neo.Plugin;

/**
 *
 * @author Troy Heninger
 */
public class Token extends Node {

    private static final Pattern linePat = Pattern.compile("\\r\\n|\\r|\\n");
    
    private final int line;

    public Token(Plugin plugin, String name, CharSequence text, int line) {
        this(plugin, name, text, line, text);
    }
    
    public Token(Plugin plugin, String name, CharSequence text, int line, Object value) {
        this(plugin, name, text, line, value, null);
    }

    public Token(Plugin plugin, String name, CharSequence text, int line, Object value, String type) {
        super(plugin, name, text, value);
        setType(type);
        this.line = line;
    }

    public int countLines() {
        java.util.regex.Matcher match = linePat.matcher(getText());
        int lines = 0;
        while (match.find()) {
            lines++;
        }
        return lines;
    }

    @Override
    public int getLine() { return line; }

}
