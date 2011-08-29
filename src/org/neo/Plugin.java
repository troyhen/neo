package org.neo;

import org.neo.lex.Lexer;
import org.neo.lex.Token;
import org.neo.parse.Rule;

/**
 *
 * @author Troy Heninger
 */
public interface Plugin extends Lexer, Rule {

    Token consume(Named named, int chars);
    Token consume(Named named, int chars, Object value);

    void open();

}
