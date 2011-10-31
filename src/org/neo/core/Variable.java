package org.neo.core;

import org.neo.util.ClassDef;
import org.neo.parse.Engine;
import org.neo.parse.Node;

/**
 *
 * @author Troy Heninger
 */
public class Variable extends CorePlugin {
    
    @Override
    public void open() {
        addKeyword("var");
        addKeyword("val");
        
        addParser("cast", "^operator_as (class_path | @enclosed_path) (start_bracket end_bracket)*");
        addParser("enclosed_path", "start_bracket (class_path | @enclosed_path) end_bracket");
        addParser("var_symbol", "!keyword_var symbol");
        addParser("var_symbol", "statement_varDeclare | statement_varAssign < !comma symbol");
        addParser("val_symbol", "!keyword_val symbol");
        addParser("val_symbol", "statement_valAssign < !comma symbol");
        addParser("statement_varDeclare", "@var_symbol @cast?");
        addParser("statement_varAssign", "@statement_varDeclare !operator_eq expression_operator");
        addParser("statement_valAssign", "@val_symbol @cast? !operator_eq expression_operator");
//        addInvalidParser("val statement requires an initial assignment",
//                "@val_symbol cast? (comma | terminator)");
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
            Engine.engine().symbolAdd(name, ClassDef.get(typeName));
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
