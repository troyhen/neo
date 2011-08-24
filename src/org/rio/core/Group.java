package org.rio.core;

import org.rio.Plugin;
import org.rio.lex.LexerChar;

/**
 *
 * @author Troy Heninger
 */
public class Group extends Plugin {

    public static final String NAME = "group";

    @Override
    public void open() {
        super.open();
        names.add(NAME);
        add(new LexerChar(this, "start.brace", '{'));
        add(new LexerChar(this, "end.brace", '}'));
        add(new LexerChar(this, "start.bracket", '['));
        add(new LexerChar(this, "end.bracket", ']'));
        add(new LexerChar(this, "start.paren", '('));
        add(new LexerChar(this, "end.paren", ')'));
    }

}
