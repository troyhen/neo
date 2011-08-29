package org.neo.back;

import org.neo.Compiler;
import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaCompilation implements Backend {

    public static ThreadLocal<CodeBuilder> output = new ThreadLocal<CodeBuilder>();

    public static CodeBuilder output() { return output.get(); }

    private JavaExpression expression = new JavaExpression();
    private JavaImport importStmt = new JavaImport();

    @Override
    public void render(Node node) {
        CodeBuilder buff;
        output.set(buff = new CodeBuilder());
        renderHeader(buff);
//        renderPackage(buff);
        renderImports(buff, node);
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
        buff.eol().println("class Main {").tabMore();
    }

    private void renderHeader(CodeBuilder buff) {
        buff.append("/* Do not edit. This file was generated automatically");
        if (Compiler.file() != null) {
            buff.append(" from ").append(Compiler.file());
        }
        buff.append(" by the neo compiler. */").eol().eol();
    }

    private void renderImports(CodeBuilder buff, Node node) {
        while (node != null) {
            if (node.getName().equals("importStatement")) importStmt.render(node);
            else {
                if (node.getFirst() != null) renderImports(buff, node.getFirst());
            }
            node = node.getNext();
        }
    }
    
    private void renderMainOpen(CodeBuilder buff) {
        buff.println("public static void main(String[] args) {").tabMore();
    }

    private void renderStatement(CodeBuilder buff, Node node) {
        while (node != null) {
            if (node.getName().equals("importStatement")) {
                // ignore imports here
            } else {
                expression.render(node);
                buff.append(";").eol();
            }
            node = node.getNext();
        }
    }

    private void renderStatements(CodeBuilder buff, Node node) {
        while (node != null) {
            buff.tab();
            renderStatement(buff, node.getFirst());
            node = node.getNext();
        }
    }
    
//    private void renderPackage(StringBuilder buff) {
//
//    }
}
