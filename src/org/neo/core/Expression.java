package org.neo.core;

import java.util.ArrayList;
import java.util.List;
import org.neo.util.ClassDef;
import org.neo.parse.Engine;
import org.neo.util.MethodDef;
import org.neo.parse.Node;
import org.neo.util.MemberDef;

/**
 *
 * @author Troy Heninger
 */
public class Expression extends CorePlugin {

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

    private final boolean isMain;

    public Expression(boolean isMain) {
        this.isMain = isMain;
    }

    public static String commonType(Node node) {
        String common = node.getTypeName();
        while (node != null) {
            String type = node.getTypeName();
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

//    private String methodSignature(Node node) {
//        StringBuilder buff = new StringBuilder();
//        buff.append(node.getValue().toString());
//        node = node.getNext();
//        buff.append('(');
//        String comma = "";
//        while (node != null) {
//            buff.append(comma);
//            comma = ",";
//            buff.append(node.getTypeName());
//            node = node.getNext();
//        }
//        buff.append(')');
//        return buff.toString();
//    }
    
    @Override
    public void open() {
        addKeyword("def");
        addKeyword("then");
        addKeyword("do");
        addKeyword("else");
        addKeyword("end");

        addParser("array", "expression- symbol- < !start_bracket (@expression !comma?)* @expression? !end_bracket");

        addParser("reference_dot", "@expression1 !operator_dot symbol");
        addParser("reference_array", "@expression1 !start_bracket @expression (!comma? @expression)* !end_bracket");

        addParser("call_dot", "@expression0 !operator_dot symbol !start_paren (@expression (!comma? @expression)*)? !end_paren");
        addParser("call_dot", "@expression0 !operator_dot symbol @expression (!comma? @expression)*");

        addParser("call_this", "symbol !start_paren (@expression (!comma? !terminator* @expression)*)? !end_paren");
        addParser("call_this", "symbol @expression ((!comma !terminator* | comma?) @expression)* > terminator | keyword_else | keyword_end");

//        addParser("expression_symbol", "keyword_var- keyword_val- keyword_def- | statement_valAssign comma < symbol");
        addParser("expression0", "array");
        addParser("expression0", "symbol");
        addParser("expression0", "!start_paren @expression !end_paren");
        addParser("expression1", "reference > operator_assign- operator_eq-");
        addParser("expression1", "call");
        addParser("expression1", "@expression0 ^cast*"); // must precede reference
        addParser("expression2", "@expression1 (^operator_pow @expression1)*");
        addParser("expression3", "@expression2 (^operator_mul @expression2)*");
        addParser("expression4", "@expression3 (^operator_add @expression3)*");
        addParser("expression5", "@expression4 (^operator_compare @expression4)?");
        addParser("expression6", "@expression5 (^operator_other @expression5)*");
        addParser("expression", "(reference | symbol) ((^operator_assign | ^operator_eq) @expression)+ | @expression6");

        if (isMain) addParser("compilation", "!terminator_bof !terminator* expression (terminator_eof- !terminator)* !terminator_eof");
    }

    public Node transform_call_this(Node start) {
        Node node = start.getFirst();
        String symbol = node.getValue().toString();
        node = node.getNext();
        List<String> argTypes = new ArrayList<String>();
        while (node != null) {
            argTypes.add(node.getTypeName());
            node = node.getNext();
        }
        MethodDef method = Engine.engine().methodFind(symbol, argTypes.toArray(new String[argTypes.size()]));
        if (method == null) throw new SymbolException("cannot find symbol: method " + symbol, start);
        start.setValue(method);
        String type = method.getReturnType().getName();
        start.setTypeName(type);
        start.getFirst().setTypeName(type);
        return start;
    }

    public Node transform_array(Node node) {
        String type = commonType(node.getFirst()) + "[]";
        if (type != null) node.setTypeName(type);
        return node;
    }

    public Node transform_call_dot(Node start) {
        Node node = start.getFirst();
        ClassDef type = ClassDef.get(node.getTypeName());
        node = node.getNext();
        String symbol = node.getValue().toString();
        node = node.getNext();
        List<String> argTypes = new ArrayList<String>();
        while (node != null) {
            argTypes.add(node.getTypeName());
            node = node.getNext();
        }
        MethodDef method = Engine.engine().methodFind(type, symbol, argTypes.toArray(new String[argTypes.size()]));
        if (method == null) throw new SymbolException("cannot find symbol: method " + symbol, start);
        start.setValue(method);
        String returnType = method.getReturnType().getName();
        start.setTypeName(returnType);
        start.get(1).setTypeName(returnType);
        if (method.isStatic() && method.getOwner().getSimpleName().equals("N")) {
            start.setName("call_this");
            Node first = start.getFirst();
            Node next = first.getNext();
            next.insertAfter(first);
        }
        return start;
    }

    public Node transform_expression(Node node) {
        String type = commonType(node.getFirst());
        if (type != null) node.setTypeName(type);
        return node;
    }

    public Node transform_reference_dot(Node start) {
        ClassDef type;
        Node node = start.getFirst();
        String typeName = node.getTypeName();
        if (typeName == null && node.isNamed("symbol")) {
            type = Engine.engine().symbolFind(node.getValue().toString());
            if (type != null) typeName = type.getName();
            if (typeName != null) node.setTypeName(typeName);
            else throw new SymbolException("unknown symbol: " + node.getValue(), node);
        }
        type = ClassDef.get(typeName);
        node = node.getNext();
        String symbol = node.getValue().toString();
        MemberDef member = Engine.engine().memberFind(type, symbol);
        if (member == null) throw new SymbolException("cannot find symbol: member " + symbol, start);
        start.setValue(member);
        String returnType = member.getReturnType().getName();
        start.setTypeName(returnType);
        start.get(1).setTypeName(returnType);
        if (member instanceof MethodDef) {
            MethodDef method = (MethodDef) member;
            start.setName("call_dot");
            if (method.isStatic() && method.getOwner().getSimpleName().equals("N")) {
                start.setName("call_this");
                Node first = start.getFirst();
                Node next = first.getNext();
                next.insertAfter(first);
            }
        }
        return start;
    }

}
