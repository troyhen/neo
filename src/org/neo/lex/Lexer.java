package org.neo.lex;

import org.neo.Named;

/**
 *
 * @author Troy Heninger
 */
public interface Lexer extends Named {

    Token nextToken();

}
