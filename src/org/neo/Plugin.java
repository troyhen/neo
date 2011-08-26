package org.neo;

import org.neo.lex.Lexer;
import org.neo.lex.Token;

/**
 *
 * @author Troy Heninger
 */
public interface Plugin extends Lexer {

    Token consume(Lexer lexer, int chars);

    void open();

}
