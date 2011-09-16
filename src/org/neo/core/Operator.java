package org.neo.core;

import org.neo.Node;
import org.neo.lex.LexerChar;
import org.neo.lex.LexerPattern;

/**
 *
 * @author Troy Heninger
 */
public class Operator extends CorePlugin {
    
    public static final String OPERATOR_OTHER = "operator.other";
    public static final String OPERATOR_AS = "operator.as";
    public static final String OPERATOR_COMPARE = "operator.compare";
    public static final String OPERATOR_ADD = "operator.add";
    public static final String OPERATOR_EQ = "operator.eq";
    public static final String OPERATOR_ASSIGN = "operator.assign";
    public static final String OPERATOR_MUL = "operator.mul";
    public static final String OPERATOR_POW = "operator.pow";
    public static final String OPERATOR_DOT = "operator.dot";

    @Override
    public void open() {
        super.open();
        names.add("operator");
        add(new LexerPattern(this, OPERATOR_COMPARE, "(<=>|<=|>=|<|>|!=|===|==|~=)"));
        add(new LexerChar(this, OPERATOR_AS, '~'));
        add(new LexerChar(this, OPERATOR_EQ, '='));
        add(new LexerPattern(this, OPERATOR_ASSIGN, "[-+~!@$%^&*/?:|]*="));
        add(new LexerPattern(this, OPERATOR_ADD, "[-+]"));
        add(new LexerPattern(this, OPERATOR_MUL, "[*/%]"));
        add(new LexerChar(this, OPERATOR_POW, '^'));
        add(new LexerChar(this, OPERATOR_DOT, '.'));
        add(new LexerPattern(this, OPERATOR_OTHER, "[-+~!@$%^&*/?:|]+"));
    }

    @Override
    public Node transform(Node node) {
        String name = node.getName();
        String type = null;
        if (name.equals("operator.compare")) {
            type = node.getValue().toString().equals("<=>") ? "int" : "boolean";
        } else if (name.equals("operator.as")) {
            type = collectType(node);
        } else if (name.equals("operator.assign") || name.equals("operator.eq")) {
            Node left = node.getFirst();
            type = node.getLast().getType();
            if (left.getName().startsWith("operator")) {
                node.insertBefore(left);
                node.addFirst(left.getLast());
                left.add(node);
                if (type != null) left.setType(type);
            }
        } else if (name.equals("array")) {
            type = Expression.commonType(node.getFirst()) + "[]";
        } else if (!name.startsWith("operator.dot")) {
            type = Expression.commonType(node.getFirst());
        }
        if (type != null) node.setType(type);
        return node;
    }

    private String collectType(Node node) {
        StringBuilder buff = new StringBuilder();
        /*if (node.getFirst() != null)*/ node = node.getFirst();    // for cast
        //else node = node.getNext(); // for type casting
        while (node != null) {// && !node.getName().startsWith("expression")) {
            buff.append(node.getText());
            node = node.getNext();
        }
        return buff.toString();
    }

}
