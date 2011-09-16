package org.neo.core;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class Methods extends CorePlugin {
    public static final String STATEMENT_RETURN = "statement.return";
    
    @Override
    public void open() {
        addKeyword("def");
        addKeyword("return");
//        addParser("statement.def",
//                "!keyword.def symbol @cast? !start.paren (symbol (@cast? !operator.eq expression | @cast) "
//                + "(!comma? !terminator* symbol (@cast? !operator.eq expression | @cast))*)? !end.paren "
//                + "(statement | !terminator @block)");
//        addParser("statement.def",
//                "!keyword.def symbol @cast? (symbol (@cast? !operator.eq expression | @cast) "
//                + "(!comma? symbol (@cast? !operator.eq expression | @cast))*)? "
//                + "!terminator @block");
        addParser("defTop",
                "!keyword.def symbol @cast? !start.paren (symbol @cast "
                + "(!comma? !terminator* symbol @cast)*)? !end.paren");
        addParser("defTop",
                "!keyword.def symbol @cast? (symbol @cast ((!comma !terminator | comma?) symbol @cast)*)?");
        addParser("statement.def",
                "@defTop (statement | !terminator @block)");
        addParser(STATEMENT_RETURN, "!keyword.return @expression? > (terminator | keyword.if | keyword.unless | keyword.while | keyword.until)");
    }

    @Override
    public Node transform(Node node) {
        String type = null;
        String name = node.getName();
        if (name.startsWith("call")) {
            type = "void";  // TODO look up actual return type
        } else if (name.equals(STATEMENT_RETURN)) {
            type = node.getFirst() == null ? "void" : node.getFirst().getType();
        } else {
            if (node.get(1).getName().equals("operator.as")) {
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
