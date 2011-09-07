package org.neo.back;

import com.sun.tools.javac.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.neo.Compiler;
import org.neo.Log;
import org.neo.NeoException;
import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaCompilation implements Backend {

    private static final boolean PLATFORM_COMPILER = false;

    public static ThreadLocal<CodeBuilder> output = new ThreadLocal<CodeBuilder>();

    public static CodeBuilder output() { return output.get(); }

    private JavaExpression expression = new JavaExpression();
    private JavaImport importStmt = new JavaImport();

    private String getClassName() {
        File file = Compiler.file();
        if (file == null) return "Main";
        String name = file.getName();
        int dot = name.indexOf('.');
        if (dot > 0) name = name.substring(0, dot);
        return name;
    }

    private void javac(String saveName) {
        String classCmd = Compiler.compiler().get("class");
        if (classCmd != null) {
            int res;
            if (PLATFORM_COMPILER) {
                JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
                if (javac == null) throw new NeoException("Missing javac tool. Make sure tools.jar is in your classpath.");
                res = javac.run(System.in, System.out, System.err, saveName);
            } else {
                res = Main.compile(new String[]{saveName});
            }
            if (res != 0) throw new NeoException("javac error: " + res);
            if (classCmd.equals("load") || classCmd.equals("run")) {
                File saveFile = new File(saveName);
                Loader load = new Loader(saveFile.getParent());
                try {
                    Class cl = load.loadClass(getClassName());
                    if (classCmd.equals("run")) {
                        run(cl);
                    }
                } catch (ClassNotFoundException ex) {
                    Log.logger.severe(ex.toString());
                    throw new NeoException(ex);
                }
            }
        }
    }

    @Override
    public void render(Node node) {
        CodeBuilder buff;
        output.set(buff = new CodeBuilder());
        renderHeader(buff);
//        renderPackage(buff);
        renderImports(buff, node);
        renderClassOpen(buff);
        renderMainOpen(buff);
        renderStatements(buff, node.getFirst());
        renderBlockClose(buff);
        renderBlockClose(buff);
        String result = buff.toString();
        Compiler.compiler().set("output", result);
        save(result);
    }

    private void renderBlockClose(CodeBuilder buff) {
        buff.tabLess().println("}");
    }

    private void renderClassOpen(CodeBuilder buff) {
        buff.eol().println("public class " + getClassName() + " {").tabMore();
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
                buff.tab();
                expression.render(node);
                buff.append(";").eol();
            }
            node = node.getNext();
        }
    }

    private void renderStatements(CodeBuilder buff, Node node) {
        while (node != null) {
            renderStatement(buff, node.getFirst());
            node = node.getNext();
        }
    }
    
//    private void renderPackage(StringBuilder buff) {
//
//    }

    private void save(String result) throws NeoException {
        String saveName = Compiler.compiler().get("save");
        if (saveName != null) {
            try {
                FileWriter out = new FileWriter(saveName);
                try {
                    out.write(result);
                } finally {
                    out.close();
                }
                javac(saveName);
            } catch (IOException ex) {
                Log.logger.severe(ex.toString());
                throw new NeoException(ex);
            }
        }
    }

    private void run(Class cl) {
        Method[] methods = cl.getDeclaredMethods();
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers()) && method.getName().equals("main")) {
                try {
                    method.invoke(null, (Object)new String[0]);
                    break;
                } catch (Exception ex) {
                    Log.logger.severe(ex.toString());
                    throw new NeoException(ex);
                }
            }
        }
    }

}
