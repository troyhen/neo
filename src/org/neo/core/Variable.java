package org.neo.core;

import org.neo.ClassDef;
import org.neo.Compiler;
import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class Variable extends CorePlugin {
    
    @Override
    public void open() {
        addKeyword("var");
        addKeyword("val");
        
        addParser("cast", "^operator_as start_bracket start_bracket start_bracket start_bracket class_path end_bracket end_bracket end_bracket end_bracket");
        addParser("cast", "^operator_as start_bracket start_bracket start_bracket class_path end_bracket end_bracket end_bracket");
        addParser("cast", "^operator_as start_bracket start_bracket class_path end_bracket end_bracket");
        addParser("cast", "^operator_as start_bracket class_path end_bracket");
        addParser("cast", "^operator_as class_path (start_bracket end_bracket)*");
        addParser("statement_varDeclare", "!keyword_var symbol @cast?");
        addParser("statement_varAssign", "@statement_varDeclare !operator_eq expression");
        addParser("statement_varDeclare",
                "(statement_varDeclare | statement_varAssign) < !comma symbol @cast?");
        addParser("statement_valAssign", "!keyword_val symbol @cast? !operator_eq expression");
        addParser("statement_valAssign",
                "statement_valAssign < !comma symbol @cast? !operator_eq expression");
        addInvalidParser("val statement requires an initial assignment",
                "keyword_val symbol cast? terminator");
    }

    public Node transform_cast(Node node) {
            /*
             * Convert:
             * cast
             *     @expression
             *     operator.as
             *         class_path
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

    public Node transform_statement(Node node) {
        final String typeName = node.get(1).getTypeName();
        final String name = node.getFirst().getValue().toString();
        if (typeName != null) {
            node.setTypeName(typeName);
            Compiler.compiler().symbolAdd(name, ClassDef.get(typeName));
        }
        return node;
    }

//    @Override
//    public Node transform(Node node) {
//        if (node.isNamed("cast")) {    // see expression: @expression ^cast
//            /*
//             * Convert:
//             * cast
//             *     @expression
//             *     operator.as
//             *         class_path
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
