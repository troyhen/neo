package org.dia.lex;

import java.util.regex.Pattern;
import org.dia.Node;

/**
 *
 * @author Troy Heninger
 */
public class Token extends Node {

    private static final Pattern linePat = Pattern.compile("\\r\\n|\\r|\\n");
    
    private final int line;
    private final Lexer lexer;

    public Token(Lexer lexer, CharSequence text, int line) {
        this(lexer, text, line, text);
    }
    
    public Token(Lexer lexer, CharSequence text, int line, Object value) {
        super(lexer, text, value);
        this.lexer = lexer;
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
    public String getName() { return lexer.getName(); }
}
