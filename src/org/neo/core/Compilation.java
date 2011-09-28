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
        
        addParser("statement_if", "statement_def- statement !keyword_if @expression");
        addParser("statement_unless", "statement_def- statement !keyword_unless @expression");
        addParser("statement_while", "statement_def- statement !keyword_while @expression");
        addParser("statement_until", "statement_def- statement !keyword_until @expression");
        addParser("statement", "keyword_if- keyword_unless- keyword_while- keyword_until- < @expression > operator- closureTop- keyword_else-");
//        addParser("callWithBlock", "call");
        addParser("statements", "(!terminator* statement)+ > terminator");
//        addParser("statements", "@statements (@statements | statement)+");
        addParser("statements", "@statements !terminator* @statements");
    }

    public void prepare_statements(Node node) {
        Node parent = node.getParent();
        parent.getPlugin().invoke("descend_", parent);
    }

    public Node transform_statement(Node node) {
        String type = node.getFirst().getTypeName();
        if (type != null) node.setTypeName(type);
        return node;
    }

    public Node transform_statements(Node node) {
        String type = node.getLast().getTypeName();
        if (type != null) node.setTypeName(type);
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
