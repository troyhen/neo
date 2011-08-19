package org.dia.lex;

/**
 *
 * @author Troy Heninger
 */
public interface Lexer {

    String getName();
    boolean isIgnored();
    Token nextToken();

}
