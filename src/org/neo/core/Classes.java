package org.neo.core;

/**
 *
 * @author Troy Heninger
 */
public class Classes extends CorePlugin {

    @Override
    public void open() {
        addKeyword("class");
        addKeyword("implements");
        
        addParser("classTop", "!keyword_class @expression_symbol @cast? > operator_as-");
        addParser("classTop", "@classTop !comma !terminator+ class_path");
        addParser("classTop", "@classTop !comma? class_path");
        addParser("statement_class", "@classTop !terminator+ @block");
    }

//    public Node transform_statement(Node node) {
//
//    }
}
