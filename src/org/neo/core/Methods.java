package org.neo.core;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class Methods extends CorePlugin {
    public static final String STATEMENT_RETURN = "statement_return";
    
    @Override
    public void open() {
        addKeyword("def");
        addKeyword("return");
        
        addParser("defTop",
                "!keyword_def symbol @cast? !start_paren (symbol @cast "
                + "(!comma? !terminator* symbol @cast)*)? !end_paren");
        addParser("defTop",
                "!keyword_def symbol @cast? (symbol @cast ((!comma !terminator | comma?) symbol @cast)*)?");
        addParser("statement_def",
                "@defTop (statement | !terminator @block)");
        addParser(STATEMENT_RETURN, "!keyword_return @expression? > (terminator | keyword_if | keyword_unless | keyword_while | keyword_until)");
    }

    public Node statement_return(Node node) {
        String type = node.getFirst() == null ? "void" : node.getFirst().getType();
        if (type != null) node.setType(type);
        return node;
    }

    @Override
    public Node transform(Node node) {
        String type = null;
        String name = node.getName();
        if (name.equals(STATEMENT_RETURN)) {
            type = node.getFirst() == null ? "void" : node.getFirst().getType();
        } else {
            if (node.get(1).getName().equals("operator_as")) {
                type = node.get(1).getType();
            } else {
                type = node.getLast().getType();
            }
            Node lastStmt = node.getLast().getLast();
            if (!type.equals("void") && !lastStmt.getName().equals(STATEMENT_RETURN)) {
                Node returnStmt = new Node(this, STATEMENT_RETURN, null, null, lastStmt.getType());
                lastStmt.append(returnStmt);
                returnStmt.addAll(lastStmt.getFirst());
                lastStmt.unlink();
            }
        }
        if (type != null) node.setType(type);
        return node;
    }

}
