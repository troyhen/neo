package org.neo;

import org.neo.parse.Transform;
import org.neo.parse.Refine;
import org.neo.parse.Node;
import org.neo.back.Render;
import org.neo.lex.Lexer;
import org.neo.lex.Token;
import org.neo.parse.Rule;

/**
 *
 * @author Troy Heninger
 */
public interface Plugin extends Lexer, Render, Rule, Transform, Refine {

//    Token consume(String name, int chars);
//    Token consume(String name, int chars, Object value);
    Token consume(String name, int chars, Object value, String type);
    Object invoke(String prefix, Node node);
    void open();

}
