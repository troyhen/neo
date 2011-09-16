package org.neo.core;

import org.neo.Node;
import org.neo.lex.LexerChar;
import org.neo.lex.LexerPattern;

/**
 *
 * @author Troy Heninger
 */
public class Operator extends CorePlugin {
    
    public static final String OPERATOR_OTHER = "operator_other";
    public static final String OPERATOR_AS = "operator_as";
    public static final String OPERATOR_COMPARE = "operator_compare";
    public static final String OPERATOR_ADD = "operator_add";
    public static final String OPERATOR_EQ = "operator_eq";
    public static final String OPERATOR_ASSIGN = "operator_assign";
    public static final String OPERATOR_MUL = "operator_mul";
    public static final String OPERATOR_POW = "operator_pow";
    public static final String OPERATOR_DOT = "operator_dot";

    public Node array(Node node) {
        String type = Expression.commonType(node.getFirst()) + "[]";
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

    public Node operator_as(Node node) {
        combineBrackets(node.getFirst(), node.getLast());
        String type = collectType(node);
        if (type != null) node.setType(type);
        return node;
    }

    /*
     * Convert:
     * ~
     *     [
     *     [
     *     symbol
     *     .
     *     symbol
     *     ]
     *     ]
     * into:
     * ~
     *     symbol
     *     .
     *     symbol
     *     [
     *     ]
     *     [
     *     ]
     */
    private void combineBrackets(Node first, Node last) {
        if (first.getName().equals("start_bracket") && last.getName().equals("end_bracket")) {
            combineBrackets(first.getNext(), last.getPrev());
            last.insertBefore(first);
        }
    }

    public Node operator_assign(Node node) {
        Node left = node.getFirst();
        String type = node.getLast().getType();
        if (left.getName().startsWith("operator")) {
            /*
             * Convert:
             * =
             *     =
             *         reference1
             *         reference2
             *     @expression
             * into:
             * =
             *     reference1
             *     =
             *         reference2
             *         @expression
             */
            node.insertBefore(left);
            node.addFirst(left.getLast());
            left.add(node);
            if (type != null) left.setType(type);
        }
        if (type != null) node.setType(type);
        return node;
    }

    public Node operator_compare(Node node) {
        String type = node.getValue().toString().equals("<=>") ? "int" : "boolean";
        if (type != null) node.setType(type);
        return node;
    }

    public Node operator_dot(Node node) {
        if (node.getFirst() != null) {
            String type = Expression.commonType(node.getFirst());
            if (type != null) node.setType(type);
        }
        return node;
    }

    public Node operator_eq(Node node) {
        return operator_assign(node);
    }


//    @Override
//    public Node transform(Node node) {
//        String name = node.getName();
//        String type = null;
//        if (name.equals("operator_compare")) {
//            type = node.getValue().toString().equals("<=>") ? "int" : "boolean";
//        } else if (name.equals("operator_as")) {
//            type = collectType(node);
//        } else if (name.equals("operator_assign") || name.equals("operator_eq")) {
//            Node left = node.getFirst();
//            type = node.getLast().getType();
//            if (left.getName().startsWith("operator")) {
//                /*
//                 * Convert:
//                 * =
//                 *     =
//                 *         reference1
//                 *         reference2
//                 *     @expression
//                 * into:
//                 * =
//                 *     reference1
//                 *     =
//                 *         reference2
//                 *         @expression
//                 */
//                node.insertBefore(left);
//                node.addFirst(left.getLast());
//                left.add(node);
//                if (type != null) left.setType(type);
//            }
//        } else if (name.equals("array")) {
//            type = Expression.commonType(node.getFirst()) + "[]";
//        } else if (!name.startsWith("operator_dot")) {
//            type = Expression.commonType(node.getFirst());
//        }
//        if (type != null) node.setType(type);
//        return node;
//    }

}
