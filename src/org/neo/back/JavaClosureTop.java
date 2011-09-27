package org.neo.back;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaClosureTop implements Backend {

    @Override
    public void render(Node node) {
//        String type = null;
//        if (node.get(0).isNamed("operator_as")) {
//            type = node.get(0).getType();
//        } else {
//            type = node.getLast().getLast().getType();
//        }
        CodeBuilder buff = JavaCompilation.output();
        buff.append("new Closure(null) {").eol().tabMore();
        buff.tab().append("public Object invoke(Object...neoClosureArguments) {").eol().tabMore();
        Node child = node.getFirst();
        Node returnType = child;
        if (child.isNamed("operator_as")) child = child.getNext();
        int ix = 0;
        while (child != node.getLast()) {
            Node symbol = child;
            Node operatorAs = child.getNext();
            buff.tab().append(operatorAs.getTypeName()).append(" ");
            symbol.render("java");
            buff.append(" = (").append(operatorAs.getTypeName()).append(") neoClosureArguments[").append(ix).append("];").eol();
            child = operatorAs.getNext();
        }
        child.render("java");
        buff.tab().append("return null;").eol();
        buff.tabLess().tab().append("}").eol();
        buff.tabLess().tab().append("}");
    }

}
