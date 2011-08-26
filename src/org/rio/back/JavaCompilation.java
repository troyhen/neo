package org.rio.back;

import org.rio.Compiler;
import org.rio.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaCompilation implements Backend {

    public static ThreadLocal<CodeBuilder> output = new ThreadLocal<CodeBuilder>();

    public static CodeBuilder output() { return output.get(); }

    @Override
    public void render(Node node) {
        CodeBuilder buff;
        output.set(buff = new CodeBuilder());
        renderHeader(buff);
//        renderPackage(buff);
        renderDefaultClassOpen(buff);
        renderMainOpen(buff);
        renderStatements(buff, node.getFirst());
        renderBlockClose(buff);
        renderBlockClose(buff);
        Compiler.compiler().set("output", buff.toString());
    }

    private void renderBlockClose(CodeBuilder buff) {
        buff.tabLess().println("}");
    }

    private void renderDefaultClassOpen(CodeBuilder buff) {
        buff.println("public class Main {").tabMore();
    }

    private void renderHeader(CodeBuilder buff) {
        buff.append("/* Do not edit. This file was generated automatically");
        if (Compiler.file() != null) {
            buff.append(" from ").append(Compiler.file());
        }
        buff.append(" by the rio compiler. */").eol().eol();
    }

    private void renderMainOpen(CodeBuilder buff) {
        buff.println("public static void main(String[] args) {").tabMore();
    }

    private void renderStatement(CodeBuilder buff, Node node) {
        JavaExpression expression = new JavaExpression();
        while (node != null) {
            expression.render(node);
            node = node.getNext();
        }
    }

    private void renderStatements(CodeBuilder buff, Node node) {
        while (node != null) {
            buff.tab();
            renderStatement(buff, node.getFirst());
            buff.append(";").eol();
            node = node.getNext();
        }
    }
    
//    private void renderPackage(StringBuilder buff) {
//
//    }
}
