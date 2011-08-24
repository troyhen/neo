package org.rio.lex;

import org.rio.Named;

/**
 *
 * @author Troy Heninger
 */
public interface Lexer extends Named {

    Token nextToken();

}
