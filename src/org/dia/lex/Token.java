package org.dia.lex;

import java.util.regex.Pattern;
import org.dia.Node;
import org.dia.Plugin;

/**
 *
 * @author Troy Heninger
 */
public class Token extends Node {

    private static final Pattern linePat = Pattern.compile("\\r\\n|\\r|\\n");

    public Token(Plugin plugin, String name, CharSequence text, int line) {
        this(plugin, name, text, line, text);
    }
    
    public Token(Plugin plugin, String name, CharSequence text, int line, Object value) {
        super(plugin, name, text, line, value);
    }

    public int countLines() {
        java.util.regex.Matcher match = linePat.matcher(getText());
        int lines = 0;
        while (match.find()) {
            lines++;
        }
        return lines;
    }
}
