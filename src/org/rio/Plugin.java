package org.rio;

import org.rio.lex.Lexer;
import org.rio.lex.Token;

/**
 *
 * @author Troy Heninger
 */
public interface Plugin extends Lexer {

    Token consume(Lexer lexer, int chars);

    void open();

}
