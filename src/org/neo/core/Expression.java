package org.neo.core;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class Expression extends CorePlugin {
    
    @Override
    public void open() {
        addParser("array", "expression- reference- symbol- < !start.bracket (@expression !comma?)* @expression? !end.bracket");
        addParser("expression", "number");
        addParser("expression", "string");
        addParser("expression", "array");
        addParser("call", "keyword.def- < symbol !start.paren (@expression (!comma? @expression)*)? !end.paren");
        addParser("expression", "call");
        addParser("expression", "!start.paren @expression !end.paren");
        addParser("expression", "@expression ^cast"); // must come before reference
        addParser("reference", "symbol > (operator | start | keyword | terminator)");
        addParser("reference.dot", "@expression ^operator.dot symbol");
        addParser("reference.array", "@expression !start.bracket @expression !end.bracket");
        addParser("expression", "@expression ^operator.pow @expression");
        addParser("expression", "@expression ^operator.mul @expression");
        addParser("expression", "@expression ^operator.add @expression");
        addParser("expression", "@expression ^operator.compare @expression");
        addParser("expression", "reference (^operator.assign | ^operator.eq) @expression");
        addParser("expression", "reference");
        addParser("call", "keyword.def- < symbol @expression (!comma? @expression)*");
        addParser("expression", "expression- < keyword.if @expression !keyword.then statement elseClause? !symbol.end?");
        addParser("expression", "expression- < keyword.if @expression !keyword.then? !terminator @block elseClause?");
        addParser("expression", "expression- < keyword.unless @expression !keyword.then statement elseClause?");
        addParser("expression", "expression- < keyword.unless @expression !keyword.then? !terminator @block elseClause? !symbol.end?");
        addParser("expression", "expression- < keyword.while @expression !keyword.do statement elseClause?");
        addParser("expression", "expression- < keyword.while @expression !keyword.do? !terminator @block elseClause? !symbol.end");
        addParser("expression", "expression- < keyword.until @expression !keyword.do statement elseClause?");
        addParser("expression", "expression- < keyword.until @expression !keyword.do? !terminator @block elseClause? !symbol.end");
        addParser("elseClause", "!keyword.else statement");
        addParser("elseClause", "!keyword.else !terminator @block");
    }

    @Override
    public Node transform(Node node) {
        String name = node.getName();
        String type = null;
        if (name.equals("array")) {
            type = commonType(node.getFirst()) + "[]";
        } else if (name.startsWith("expression")) {
            type = commonType(node.getFirst());
        } else if (name.startsWith("call")) {
            type = "void";  // TODO look up actual return type
        }
        if (type != null) node.setType(type);
        return node;
    }

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

}
