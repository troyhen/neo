package org.neo.lex;

import java.util.regex.Pattern;
import org.neo.parse.Node;
import org.neo.Plugin;

/**
 *
 * @author Troy Heninger
 */
public class Token extends Node {

    private static final Pattern linePat = Pattern.compile("\\r\\n|\\r|\\n");
    
    private final int line;

    public Token(Plugin plugin, String name/*, int index*/, CharSequence text, int line) {
        this(plugin, name/*, index*/, text, line, text, null);
    }
    
    public Token(Plugin plugin, String name/*, int index*/, CharSequence text, int line, Object value) {
        this(plugin, name/*, index*/, text, line, value, null);
    }

    public Token(Plugin plugin, String name/*, int index*/, CharSequence text, int line, Object value, String type) {
        super(plugin, name/*, index*/, text, value, type);
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
