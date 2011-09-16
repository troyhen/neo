package org.neo.core;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class Compilation extends CorePlugin {

    @Override
    public void open() {
//        addParser("statement", "alias");

        addKeyword("if");
        addKeyword("unless");
        addKeyword("while");
        addKeyword("until");
        
        addParser("statement_if", "statement !keyword_if @expression");
        addParser("statement_unless", "statement !keyword_unless @expression");
        addParser("statement_while", "statement !keyword_while @expression");
        addParser("statement_until", "statement !keyword_until @expression");
        addParser("statement", "@expression");
//        addParser("callWithBlock", "call");
        addParser("statements", "!terminator* (statement !terminator+)+");
    }

    public Node statement(Node node) {
        String type = node.getFirst().getType();
        if (type != null) node.setType(type);
        return node;
    }

    public Node statements(Node node) {
        String type = node.getLast().getType();
        if (type != null) node.setType(type);
        return node;
    }
    
//    @Override
//    public Node transform(Node node) {
//        String name = node.getName();
//        String type = null;
//        if (name.equals("statements")) {
//            type = node.getLast().getType();
//        } else if (name.equals("statement")) {
//            type = node.getFirst().getType();
//        }
//        if (type != null) node.setType(type);
//        return node;
//    }

}
