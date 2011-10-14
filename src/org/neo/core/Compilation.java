package org.neo.core;

import org.neo.parse.Node;

/**
 *
 * @author Troy Heninger
 */
public class Compilation extends CorePlugin {

    @Override
    public void open() {
        addParser("statement", "keyword_if- keyword_unless- keyword_while- keyword_until- < @expression > operator- closureTop- keyword_else-");
        addParser("statements", "(!terminator* statement)+ > terminator");
        addParser("statements", "@statements !terminator* (@statements | statement)");
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

}
