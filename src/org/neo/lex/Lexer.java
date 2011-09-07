package org.neo.lex;

import org.neo.Plugin;

/**
 *
 * @author Troy Heninger
 */
public interface Lexer {

    String getName();
    Plugin getPlugin();
    Token nextToken();

}
