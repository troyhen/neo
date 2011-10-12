package org.neo.back;

import com.sun.tools.javac.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

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

//    public static final String HEADER = "%header";
    public static final String OUTSIDE = "%outside";
    public static final String MAIN = "%main";
    public static final String INSIDE = "%inside";
//    public static enum Segment {outside, members, main, current;
//        int index = segments++;
//    };

    private static final boolean PLATFORM_COMPILER = false;

    public static ThreadLocal<Map<String, CodeBuilder>> output = new ThreadLocal<Map<String, CodeBuilder>>();

    private static Stack<String> segments = new Stack<String>();

    public static CodeBuilder output() {
        if (segments.isEmpty()) {
            openSegment(OUTSIDE);
            openSegment(INSIDE);
            openSegment(MAIN);
        }
        return output(segments.peek());
    }
    
    public static CodeBuilder output(String segment) {
        Map<String, CodeBuilder> map = output.get();
        if (map == null) {
            output.set(map = new HashMap<String, CodeBuilder>());
        }
        CodeBuilder buff = map.get(segment);
        if (buff == null) {
            buff = new CodeBuilder();
            map.put(segment, buff);
            if (OUTSIDE.equals(segment)) renderHeader(buff);
            else if (INSIDE.equals(segment)) renderClassOpen(buff);
            else if (MAIN.equals(segment)) renderMainOpen(buff);
            else {
                CodeBuilder outside = output(OUTSIDE);
                if (outside != null && outside.length() > 0) {
                    buff.append(outside);
                }
            }
        }
        return buff;
    }

    public static void closeSegment() {
        segments.pop();
    }

    public static CodeBuilder openSegment(String segment) {
//        String tabs = null;
//        if (segments.size() > 0 && segment.startsWith("%"))
//            tabs = output().getTabs();
        if (INSIDE.equals(segment) && segments.size() > 0 && !segments.peek().startsWith("%")) segment = segments.peek();
        segments.push(segment);
        CodeBuilder buff = output();
//        if (tabs != null && tabs.length() > 0)
//            buff.setTabs(tabs + buff.getTabs());
        return buff;
    }

//    private JavaExpression expression = new JavaExpression();
//    private JavaStatementImport importStmt = new JavaStatementImport();

    private static String getClassName() {
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
                    Log.error(ex);
                    throw new NeoException(ex);
                }
            }
        }
    }

    @Override
    public void render(Node node) {
        output.set(null);
//        CodeBuilder outside  = output();
//        renderPackage(buff);
//        renderImports(buff, node);
        node.getFirst().render("java");
//        Map<String, CodeBuilder> map = output.get();
//        CodeBuilder main = map.get(MAIN);
//        if (main != null && main.length() > 0) renderBlockClose(main);
//        CodeBuilder inside = map.get(INSIDE);
//        if (inside != null && inside.length() > 0) renderBlockClose(inside);
//        CodeBuilder outside  = openSegment(OUTSIDE);
//        renderBlockClose(inside);
        if (!segments.isEmpty()) {
            String innerSegment = segments.peek();
            do {
                String segment = segments.peek();
                closeSegment();
                if (MAIN.equals(segment) || INSIDE.equals(segment)) {
                    renderBlockClose(output(innerSegment));
                }
            } while (!segments.isEmpty());
        }
        String result = toString();
        Compiler.compiler().set("output", result);
        save(result);
    }

    private static void renderBlockClose(CodeBuilder buff) {
        buff.tabLess().println("}").eol();
    }

    private static void renderClassOpen(CodeBuilder buff) {
        buff.eol().println("public class " + getClassName() + " {").eol().tabMore();
    }

    private static void renderHeader(CodeBuilder buff) {
        buff.append("/* Do not edit. This file was generated automatically");
        if (Compiler.file() != null) {
            buff.append(" from ").append(Compiler.file());
        }
        buff.append(" by the neo compiler. */").eol().eol();
        buff.append("import neo.lang.*;").eol();
        buff.append("import static neo.lang.N.*;").eol();
    }

//    private void renderImports(CodeBuilder buff, Node node) {
//        while (node != null) {
//            if (node.isNamed(Import.STATEMENT)) importStmt.render(node);
//            else {
//                if (node.getFirst() != null) renderImports(buff, node.getFirst());
//            }
//            node = node.getNext();
//        }
//    }
    
    private static void renderMainOpen(CodeBuilder buff) {
        buff.tabMore().println("public static void main(String[] args) {").tabMore();
    }

//    private void renderStatement(CodeBuilder buff, Node node) {
//        while (node != null) {
//            if (node.isNamed(Import.STATEMENT)) {
//                // ignore imports here
//            } else {
//                buff.tab();
//                expression.render(node.getFirst());
//                buff.append(";").eol();
//            }
//            node = node.getNext();
//        }
//    }

//    private void renderStatements(CodeBuilder buff, Node node) {
//        while (node != null) {
//            renderStatement(buff, node.getFirst());
//            node = node.getNext();
//        }
//    }
    
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
                Log.error(ex);
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
                } catch (NeoException e) {
                    throw e;
                } catch (Exception ex) {
                    while (ex.getCause() != null) ex = (Exception) ex.getCause();
                    Log.error(ex);
                    throw new NeoException(ex);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        Map<String, CodeBuilder> map = output.get();
        final CodeBuilder outside = map.get(OUTSIDE);
        final CodeBuilder inside = map.get(INSIDE);
        final CodeBuilder main = map.get(MAIN);
        if (inside != null && inside.length() > 0) {
            buf.append(outside);
            buf.append(inside);
        }
        if (main != null && main.length() > 0) buf.append(main);
        for (String name : map.keySet()) {
            if (name.startsWith("%")) continue;
            buf.append(map.get(name));
        }
        return buf.toString();
    }

}
