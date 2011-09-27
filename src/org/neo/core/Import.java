package org.neo.core;

import org.neo.Compiler;
import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class Import extends CorePlugin {

    public static final String NAME = "import";
    public static final String STATEMENT = "statement_import";

    public static String getPath(Node node) {
        StringBuilder buff = new StringBuilder();
        while (node != null) {
            buff.append(node.getValue());
            node = node.getNext();
        }
        return buff.toString();
    }

    @Override
    public void open() {
        super.open();
        names.add(NAME);
        addKeyword(NAME);
        addParser(STATEMENT, "!keyword_import (symbol operator_dot)* symbol");
    }

//    public void transform_statement(Node node) {
//        Compiler.compiler().importPackage(node.getValue().toString());
////            try {
////                Class[] classes = PackageTool.getClasses(node.getValue().toString());
////                for (Class type : classes) {
////                    String name = type.getName();
////                    int dot = name.lastIndexOf('.');
////                    if (dot > 0) name = name.substring(dot + 1);
////                    Compiler.compiler().symbolAdd(name, type.getSimpleName());
////                    if ("N".equals(name)) {
////                        Method[] methods = type.getMethods();
////                        for (Method method : methods) {
////                            final int modifiers = method.getModifiers();
////                            if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers)) {
////                                String signature = methodSignature(method);
////                                Compiler.compiler().symbolAdd(signature, method.getReturnType().getSimpleName());
////                            }
////                        }
////                    }
////                }
////            } catch (ClassNotFoundException ex) {
////                Log.error(ex);
////            } catch (IOException ex) {
////                Log.error(ex);
////            } catch (URISyntaxException ex) {
////                Log.error(ex);
////            }
//    }

    public Node transform_statement_import(Node node) {
        String value = getPath(node.getFirst());
        node.setValue(value);
        Compiler.compiler().importPackage(value);
        return node;
    }

}
