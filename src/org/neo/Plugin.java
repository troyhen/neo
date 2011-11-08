package org.neo;

import org.neo.parse.*;
import org.neo.back.Render;
import org.neo.lex.Lexer;
import org.neo.lex.Token;

import java.util.List;

/**
 *
 * @author Troy Heninger
 */
public interface Plugin extends Lexer, Render, Rule, Transform, Refine {

//    Token consume(String name, int chars);
//    Token consume(String name, int chars, Object value);
    Token consume(String name, int chars, Object value, String type);
    void collect(String name, List<Production> list);
    void indexProductions();
    Object invoke(String prefix, Node node);
    void open();
}
