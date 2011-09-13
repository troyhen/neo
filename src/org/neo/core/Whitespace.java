package org.neo.core;

import org.neo.lex.LexerIndent;
import org.neo.lex.LexerPattern;

/**
 *
 * @author Troy Heninger
 */
public class Whitespace extends CorePlugin {

    public static final String EOL = "eol";
    public static final String END_BLOCK = "end.block";
    public static final String SAME_BLOCK = "!same.block";
    public static final String START_BLOCK = "start.block";

    @Override
    public void open() {
        names.add("ws");
        add(new LexerPattern(this, EOL, "(\\r\\n|\\r|\\n)"));   // need parens around regex because base class prepends ^
        add(new LexerPattern(this, "!comment.multiline", "[ \\t]*###+(##[^#]|#[^#]|[^#])*###+"));
        add(new LexerPattern(this, "!comment.line", "[ \\t]*#[^\\r\\n]*"));
        add(new LexerIndent(this, EOL, START_BLOCK, SAME_BLOCK, END_BLOCK)); // must come after comment lexers since they consume indented comments
        add(new LexerPattern(this, "!space", "[ \\t]+"));   // must come after indent lexer
        addParser("terminator", "semi");
        addParser("terminator", "eol");
        addParser("terminator", "eof");
        addParser("block", "!start.block statements !end.block !symbol.end?");
    }

}
