package org.dia.core;

import org.dia.Plugin;
import org.dia.lex.LexerPattern;

/**
 *
 * @author Troy Heninger
 */
public class Whitespace extends Plugin {

    public static final String EOL = "eol";

    @Override
    public void open() {
        names.add("ws");
        add(new LexerPattern(this, EOL, "(\\r\\n|\\r|\\n)"));
        add(new LexerPattern(this, "!comment.multiline", "/\\*([^*]|\\*[^/])*\\*/"));
        add(new LexerPattern(this, "!comment.line", "//[^\\r\\n]*"));
        add(new LexerPattern(this, "!space", "[ \\t]+"));
    }

}
