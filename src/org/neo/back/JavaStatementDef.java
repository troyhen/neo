package org.neo.back;

import org.neo.parse.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaStatementDef implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.openSegment(JavaCompilation.INSIDE);
        buff.tab().append("public ").append(node.getTypeName()).append(" ");
        Node child = node.getFirst();
        child.render("java");
        buff.append("(");
        child = child.getNext();
        if (child.isNamed("operator_as")) child = child.getNext();
        String comma = "";
        while (child != node.getLast()) {
            buff.append(comma).append(child.getNext().getTypeName()).append(" ");
            comma = ", ";
            child.render("java");
            child = child.getNext().getNext();
        }
        buff.append(") {").eol().tabMore();
        child.render("java");
        buff.tabLess().tab().append("}").eol().eol();
        JavaCompilation.closeSegment();
    }

}
