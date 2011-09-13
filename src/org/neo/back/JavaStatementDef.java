package org.neo.back;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaStatementDef implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.openSegment();
        buff.tab().append(node.getType()).append(" ");
        Node child = node.getFirst();
        child.render("java");
        buff.append("(");
        child = child.getNext();
        if (child.getName().equals("operator.as")) child = child.getNext();
        String comma = "";
        while (child != node.getLast()) {
            buff.append(comma).append(child.getNext().getType()).append(" ");
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
