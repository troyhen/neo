package org.dia.lex;

import org.dia.Named;

/**
 *
 * @author Troy Heninger
 */
public interface Lexer extends Named {

    Token nextToken();

}
