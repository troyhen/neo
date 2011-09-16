package org.neo.core;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class Variable extends CorePlugin {

    public Node cast(Node node) {
            /*
             * Convert:
             * cast
             *     @expression
             *     operator.as
             *         symbol
             *         ...
             * into:
             * operator.as
             *     @expression
             */
            Node operatorAs = node.get(1);
            while (operatorAs.getFirst() != null) {
                operatorAs.getFirst().unlink();
            }
            operatorAs.addFirst(node.getFirst());
            node.insertAfter(operatorAs);
            node.unlink();
            return operatorAs;
    }
    
    @Override
    public void open() {
        addKeyword("var");
        addKeyword("val");
        
        addParser("cast", "^operator_as start_bracket start_bracket start_bracket start_bracket symbol (operator_dot symbol)* end_bracket end_bracket end_bracket end_bracket");
        addParser("cast", "^operator_as start_bracket start_bracket start_bracket symbol (operator_dot symbol)* end_bracket end_bracket end_bracket");
        addParser("cast", "^operator_as start_bracket start_bracket symbol (operator_dot symbol)* end_bracket end_bracket");
        addParser("cast", "^operator_as start_bracket symbol (operator_dot symbol)* end_bracket");
        addParser("cast", "^operator_as symbol (operator_dot symbol)* (start_bracket end_bracket)*");
        addParser("statement_varDeclare",
                "!keyword_var symbol @cast?");// (start_bracket @expression end_bracket)?");
        addParser("statement_varAssign",
                "@statement_varDeclare !operator_eq expression");
        addParser("statement_valDeclare",
                "!keyword_val symbol @cast?");// (start_bracket @expression end_bracket)?");
        addParser("statement_valAssign",
                "@statement_valDeclare !operator_eq expression");
    }

//    @Override
//    public Node transform(Node node) {
//        if (node.getName().equals("cast")) {    // see expression: @expression ^cast
//            /*
//             * Convert:
//             * cast
//             *     @expression
//             *     operator.as
//             *         symbol
//             *         ...
//             * into:
//             * operator.as
//             *     @expression
//             */
//            Node operatorAs = node.get(1);
//            while (operatorAs.getFirst() != null) {
//                operatorAs.getFirst().unlink();
//            }
//            operatorAs.addFirst(node.getFirst());
//            node.insertAfter(operatorAs);
//            node.unlink();
//            return operatorAs;
//        }
//        return super.transform(node);
//    }

}
