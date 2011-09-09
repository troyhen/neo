package org.neo;

import org.neo.back.Render;
import org.neo.lex.Lexer;
import org.neo.lex.Token;
import org.neo.parse.Rule;

/**
 *
 * @author Troy Heninger
 */
public interface Plugin extends Lexer, Render, Rule, Transform {

    Token consume(String name, int chars);
    Token consume(String name, int chars, Object value);
    Token consume(String name, int chars, Object value, String type);

    void open();

}
