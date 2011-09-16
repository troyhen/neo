package org.neo.core;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class Expression extends CorePlugin {

    public Node array(Node node) {
        String type = commonType(node.getFirst()) + "[]";
        if (type != null) node.setType(type);
        return node;
    }

    public Node call(Node node) {
        node.setType("void");
        return node;
    }

    public static String commonType(Node node) {
        String common = node.getType();
        while (node != null) {
            String type = node.getType();
            if (type == null)
                common = "java.lang.Object";
            else if (!type.equals(common)) {
                boolean found = false;
                for (int ix = 0, iz = conversions.length; ix < iz; ) {
                    String type1 = conversions[ix++];
                    String type2 = conversions[ix++];
                    String type3 = conversions[ix++];
                    if (common.equals(type1) && type.equals(type2)
                            || common.equals(type2) && type.equals(type1)) {
                        common = type3;
                        found = true;
                    }
                }
                if (!found) common = "java.lang.Object";
            }
            node = node.getNext();
        }
        return common;
    }

    public Node expression(Node node) {
        String type = commonType(node.getFirst());
        if (type != null) node.setType(type);
        return node;
    }
    
    @Override
    public void open() {
        addKeyword("def");
        addKeyword("then");
        addKeyword("do");
        addKeyword("else");
        addKeyword("end");

        addParser("expression_symbol", "symbol");
        addParser("expression", "reference > operator_assign- operator_eq-");
        addParser("expression", "call");
        addParser("array", "expression- reference- < !start_bracket (@expression !comma?)* @expression? !end_bracket");
        addParser("expression", "array");

        addParser("expression", "expression_symbol- < !start_paren @expression !end_paren");
        addParser("expression", "@expression ^cast"); // must precede reference

        addParser("call_dot", "@expression !operator_dot @expression_symbol !start_paren (@expression (!comma? @expression)*)? !end_paren");
        addParser("call_dot", " start_bracket- < @expression !operator_dot @expression_symbol @expression (!comma? @expression)*");
        addParser("reference_dot", "@expression !operator_dot @expression_symbol > start_paren- expression-");
        addParser("reference_array", "@expression !start_bracket @expression (!comma? @expression)* !end_bracket");

        addParser("expression", "@expression ^operator_pow @expression");
        addParser("expression", "@expression ^operator_mul @expression");
        addParser("expression", "@expression ^operator_add @expression");
        addParser("expression", "@expression ^operator_compare @expression");
        addParser("expression", "@expression ^operator_other @expression");
        addParser("expression_assign", "(reference | @expression_symbol | @expression_assign) "
                + "(^operator_assign | ^operator_eq) @expression"); // must precede expression: reference
        addParser("expression", "expression- < keyword_if @expression !keyword_then statement elseClause? (!keyword_end keyword_if?)?");
        addParser("expression", "expression- < keyword_if @expression !keyword_then? !terminator @block elseClause? (!keyword_end keyword_if?)?");
        addParser("expression", "expression- < keyword_unless @expression !keyword_then statement elseClause? (!keyword_end keyword_unless?)?");
        addParser("expression", "expression- < keyword_unless @expression !keyword_then? !terminator @block elseClause? (!keyword_end keyword_unless?)?");
        addParser("expression", "expression- < keyword_while @expression !keyword_do statement elseClause? (!keyword_end keyword_while?)?");
        addParser("expression", "expression- < keyword_while @expression !keyword_do? !terminator @block elseClause? (!keyword_end keyword_while?)?");
        addParser("expression", "expression- < keyword_until @expression !keyword_do statement elseClause? (!keyword_end keyword_until?)?");
        addParser("expression", "expression- < keyword_until @expression !keyword_do? !terminator @block elseClause? (!keyword_end keyword_until?)?");

        addParser("elseClause", "!keyword_else statement");
        addParser("elseClause", "!keyword_else !terminator @block");

        addParser("call_this", "keyword_def- operator_dot- < @expression_symbol !start_paren (@expression (!comma? @expression)*)? !end_paren");
        addParser("call_this", "keyword_def- start_bracket- operator_dot- < @expression_symbol @expression (!comma? @expression)*");
    }

    public Node reference(Node node) {
        node.setType("void");
        return node;
    }

//    @Override
//    public Node transform(Node node) {
//        String name = node.getName();
//        String type = null;
//        if (name.equals("array")) {
//            type = commonType(node.getFirst()) + "[]";
//        } else if (name.startsWith("expression")) {
//            type = commonType(node.getFirst());
//        } else if (name.startsWith("reference")) {
//            type = "void";  // TODO look up actual type
//        } else if (name.startsWith("call")) {
//            type = "void";  // TODO look up actual return type
//        }
//        if (type != null) node.setType(type);
//        return node;
//    }

    private static final String[] conversions = new String[] {
        "char", "byte", "int",
        "short", "byte", "short",
        "short", "char", "int",
        "int", "byte", "int",
        "int", "char", "int",
        "int", "short", "int",
        "long", "byte", "long",
        "long", "char", "long",
        "long", "short", "long",
        "long", "int", "long",
        "float", "byte", "float",
        "float", "short", "float",
        "float", "int", "float",
        "float", "long", "float",
        "double", "byte", "double",
        "double", "short", "double",
        "double", "int", "double",
        "double", "long", "double",
        "double", "float", "double",
        "byte", "java.lang.Byte", "byte",
        "char", "java.lang.Character", "char",
        "short", "java.lang.Short", "short",
        "int", "java.lang.Integer", "int",
        "long", "java.lang.Long", "long",
        "boolean", "java.lang.Boolean", "boolean",
        "float", "java.lang.Float", "float",
        "double", "java.lang.Double", "double",
    };

}
