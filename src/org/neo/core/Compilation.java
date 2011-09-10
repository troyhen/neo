package org.neo.core;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class Compilation extends CorePlugin {

    @Override
    public void open() {
//        add(new LexerEof(this));
//        addParser("statement", "callWithBlock");
//        addParser("statement", "alias");

        addParser("statement.if", "statement !keyword.if @expression");
        addParser("statement.unless", "statement !keyword.unless @expression");
        addParser("statement.while", "statement !keyword.while @expression");
        addParser("statement.until", "statement !keyword.until @expression");
        addParser("statement", "@expression");
//        addParser("callWithBlock", "call");
        addParser("statements", "!terminator* (statement !terminator+)+");
    }

    @Override
    public Node transform(Node node) {
        String name = node.getName();
        String type = null;
        if (name.equals("statements")) {
            type = node.getLast().getType();
        } else if (name.equals("statement")) {
            type = node.getFirst().getType();
        }
        if (type != null) node.setType(type);
        return node;
    }

}
