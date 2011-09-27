package org.neo.core;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.neo.ClassDef;
import org.neo.Compiler;
import org.neo.MethodDef;
import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class Methods extends CorePlugin {

    public static final String STATEMENT_RETURN = "statement_return";

    private MethodDef buildMethod(Node node) {
        String name = null;
        if (node.getParent().isNamed("statement") && node.isNamed("symbol")) {
            name = node.getValue().toString();
            node = node.getNext();
        }
        String returnType = "Object";
        if (node.isNamed("operator_as")) {
            returnType = node.getTypeName();
            node = node.getNext();
        }
        List<ClassDef> args = new ArrayList<ClassDef>();
        while (node.getNext() != null) {
            Node symbol = node;
            Node typeNode = symbol.getNext();
            ClassDef type = ClassDef.get(typeNode.getTypeName());
            Compiler.compiler().symbolAdd(symbol.getValue().toString(), type);
            args.add(type);
            node = typeNode.getNext();
        }
        return new MethodDef(Compiler.currentClass(), name, ClassDef.get(returnType),
                Modifier.PUBLIC, args.toArray(new ClassDef[args.size()]));
    }

    public void descend_statement_def(Node node) {
        MethodDef def = buildMethod(node.getFirst());
        node.setValue(def);
        Compiler.compiler().methodAdd(def.getName(), def);
    }
    
    @Override
    public void open() {
        addKeyword("def");
        addKeyword("return");
        
        addParser("closureTop",
                "(expression | reference | call | symbol) < !keyword_def @cast? !start_paren (symbol @cast "
                + "(!comma? !terminator* symbol @cast)*)? !end_paren");
        addParser("closureTop",
                "(expression | reference | call | symbol) < !keyword_def @cast? (symbol @cast ((!comma !terminator+ | !comma?) symbol @cast)*)?");
        addParser("expression_closure",
                "^closureTop (statement | !terminator @block)");

        addParser("defTop",
                "expression- reference- call- symbol- < !keyword_def symbol @cast? !start_paren (symbol @cast "
                + "(!comma? !terminator* symbol @cast)*)? !end_paren");
        addParser("defTop",
                "expression- reference- call- symbol- < !keyword_def symbol @cast? (symbol @cast ((!comma !terminator+ | !comma?) symbol @cast)*)?");
        addParser("statement_def",
                "@defTop (statement | !terminator @block)");
        addParser(STATEMENT_RETURN, "!keyword_return @expression? > (terminator | keyword_if | keyword_unless | keyword_while | keyword_until)");
    }

    public void prepare_statement_def(Node node) {
        Compiler.compiler().symbolsPush();
    }

    public Node transform_closureTop(Node start) {
        String type = null;
//        if (start.get(0).isNamed("operator_as")) {
//            type = start.get(0).getType();
//        } else {
//            type = start.getLast().getLast().getType();
//        }
        type = "neo.lang.Closure";
        if (type != null) start.setTypeName(type);
        Node node = start.getFirst();
        while (node != null) {
            if (node.isNamed("symbol")) {
                String name = node.getValue().toString();
                node = node.getNext();
                type = node.getTypeName();
                Compiler.compiler().symbolAdd(name, ClassDef.get(type));
            }
            node = node.getNext();
        }
        return start;
    }

    public Node transform_statement_def(Node node) {
        Compiler.compiler().symbolsPop();
        String type = null;
        if (node.get(1).isNamed("operator_as")) {
            type = node.get(1).getTypeName();
        } else {
            type = node.getLast().getTypeName();
        }
        Node lastStmt = node.getLast().getLast();
        if (!type.equals("void") && !lastStmt.isNamed(STATEMENT_RETURN)) {
            Node returnStmt = new Node(this, STATEMENT_RETURN, null, null, lastStmt.getTypeName());
            lastStmt.append(returnStmt);
            returnStmt.addAll(lastStmt.getFirst());
            lastStmt.unlink();
        }
        if (type != null) {
            node.setTypeName(type);
//            MethodDef def = (MethodDef) node.getValue();
//            def.getName();
        }
        return node;
    }

    public Node transform_statement_return(Node node) {
        final Node first = node.getFirst();
        String type;
        if (first == null) {
            type = "void";
        } else {
            type = first.getTypeName();
//            if (type == null && first.isNamed("symbol")) {
//                String name = first.getValue().toString();
//                ClassDef typeClass = Compiler.compiler().symbolFind(name);
//                if (typeClass != null) {
//                    type = typeClass.getName();
//                }
//            }
        }
        if (type != null) node.setTypeName(type);
        return node;
    }

//    @Override
//    public Node transform(Node node) {
//        String type = null;
//        String name = node.getName();
//        if (name.equals(STATEMENT_RETURN)) {
//            return super.transform(node);
//        } else {
//            if (node.get(1).isNamed("operator_as")) {
//                type = node.get(1).getType();
//            } else {
//                type = node.getLast().getType();
//            }
//            Node lastStmt = node.getLast().getLast();
//            if (!type.equals("void") && !lastStmt.isNamed(STATEMENT_RETURN)) {
//                Node returnStmt = new Node(this, STATEMENT_RETURN, null, null, lastStmt.getType());
//                lastStmt.append(returnStmt);
//                returnStmt.addAll(lastStmt.getFirst());
//                lastStmt.unlink();
//            }
//        }
//        if (type != null) node.setType(type);
//        return node;
//    }

}
