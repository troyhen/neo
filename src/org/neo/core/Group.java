package org.neo.core;

import org.neo.lex.LexerChar;

/**
 *
 * @author Troy Heninger
 */
public class Group extends CorePlugin {

    public static final String NAME = "group";

    @Override
    public void open() {
        super.open();
        names.add(NAME);
        add(new LexerChar(this, "start_brace", '{'));
        add(new LexerChar(this, "end_brace", '}'));
        add(new LexerChar(this, "start_bracket", '['));
        add(new LexerChar(this, "end_bracket", ']'));
        add(new LexerChar(this, "start_paren", '('));
        add(new LexerChar(this, "end_paren", ')'));
    }

}
