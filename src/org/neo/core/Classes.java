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
        
        addParser("classTop", "!keyword_class symbol @cast? (!comma !terminator+ class_path | !comma? class_path)*");
        addParser("statement_class", "@classTop @block");
    }

//    public Node transform_statement(Node node) {
//
//    }
}
