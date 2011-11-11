package org.neo.core;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.neo.util.ClassDef;
import org.neo.parse.Engine;
import org.neo.util.Log;
import org.neo.util.MethodDef;
import org.neo.parse.Node;

/**
 *
 * @author Troy Heninger
 */
public class Methods extends CorePlugin {

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
            Engine.engine().symbolAdd(symbol.getValue().toString(), type);
            args.add(type);
            node = typeNode.getNext();
        }
        return new MethodDef(Engine.currentClass(), name, ClassDef.get(returnType),
                Modifier.PUBLIC, args.toArray(new ClassDef[args.size()]));
    }

    public void descend_statement_def(Node node) {
        MethodDef def = buildMethod(node.getFirst());
        node.setValue(def);
        Engine.engine().methodAdd(def.getName(), def);
        Log.info(def.toString());
        Engine.engine().symbolsPush();
    }
    
    @Override
    public void open() {
        addKeyword("def");
        addKeyword("return");

        addParser("arguments_paren", "!start_paren (symbol @cast (!comma? !terminator* symbol @cast)*)? !end_paren");
        addParser("arguments_noParen", "(symbol @cast ((!comma !terminator+ | !comma?) symbol @cast)*)? > terminator");
        addParser("closureTop", "!keyword_def @cast? @arguments");
        addParser("expression0", "^closureTop (statement | @block)");

        addParser("defTop", "!keyword_def symbol @cast? @arguments");
        addParser("statement_def", "@defTop (statement | @block)");
        addParser("statement_return", "!keyword_return @expression?");
    }

//    public void prepare_statement_def(Node node) {
//        Compiler.compiler().symbolsPush();
//    }

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
                Engine.engine().symbolAdd(name, ClassDef.get(type));
            }
            node = node.getNext();
        }
        return start;
    }

    public Node transform_statement_def(Node node) {
        Engine.engine().symbolsPop();
        String type = null;
        if (node.get(1).isNamed("operator_as")) {
            type = node.get(1).getTypeName();
        } else {
            type = node.getLast().getTypeName();
        }
        Node lastStmt = node.getLast().getLast();
        if (!type.equals("void") && !lastStmt.isNamed("statement_return")) {
            Node returnStmt = new Node(this, "statement_return", node.getIndex(), null, null, lastStmt.getTypeName());
            lastStmt.append(returnStmt);
            Node lastContent = lastStmt.getFirst();
            if (lastStmt.getName().startsWith("statement_va")) {
                lastContent = new Node(lastContent);
                returnStmt.addAll(lastContent);
            } else {
                returnStmt.addAll(lastContent);
                lastStmt.unlink();
            }
        }
        if (type != null) {
            node.setTypeName(type);
            MethodDef def = (MethodDef) node.getValue();
            def.setReturnType(ClassDef.get(type));
            Log.info(def.toString());
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
