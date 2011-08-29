package org.neo.lex;

import java.util.regex.Pattern;
import org.neo.Named;
import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class Token extends Node {

    private static final Pattern linePat = Pattern.compile("\\r\\n|\\r|\\n");
    
    private final int line;
    private final Named named;

    public Token(Named named, CharSequence text, int line) {
        this(named, text, line, text);
    }
    
    public Token(Named named, CharSequence text, int line, Object value) {
        super(named, text, value);
        this.named = named;
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

    @Override
    public String getName() { return named.getName(); }
}
