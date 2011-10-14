package org.neo.back;

import org.neo.Compiler;
import org.neo.parse.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaStatementClass implements Backend {

    @Override
    public void render(Node node) {
        Node name = node.getFirst();
        CodeBuilder buff = JavaCompilation.openSegment(name.getValue().toString());
        buff.eol().append("public class ");
        name.render("java");
        Node next = name.getNext();
        if (next.isNamed("operator_as")) {
            buff.append(" extends ").append(next.getTypeName());
            next = next.getNext();
        }
        if (next.isNamed("class_path")) {
            buff.append(" implements ");
            String comma = "";
            do {
                buff.append(comma).append(next.getTypeName());
                comma = ", ";
                next = next.getNext();
            } while (next.isNamed("class_path"));
        }
        buff.append(" {").eol().eol().tabMore();
        next.render("java");
        buff.tabLess().tab().append("}").eol();
        JavaCompilation.closeSegment();
    }

 }
