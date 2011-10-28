package org.neo.core;

import org.neo.parse.Engine;
import org.neo.parse.Node;

/**
 *
 * @author Troy Heninger
 */
public class Import extends CorePlugin {

    public static final String NAME = "import";
    public static final String PACKAGE = "package";
    public static final String STATEMENT = "statement_import";
    public static final String PACKAGE_STATEMENT = "statement_package";

    private static String getPath(Node node) {
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
        addKeyword(PACKAGE);
        addParser("class_path", "operator_as start_bracket* | keyword_package | keyword_import | terminator? classTop comma? < "
                + "symbol (operator_dot symbol)*");
        addParser(PACKAGE_STATEMENT, "!keyword_package class_path");
        addParser(STATEMENT, "!keyword_import class_path");
    }

    public Node transform_class_path(Node node) {
        String path = getPath(node.getFirst());
        node.setTypeName(path);
        while (node.getLast() != null) {
            node.getLast().unlink();
        }
        return node;
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
        String value = node.getFirst().getTypeName();
        node.setValue(value);
        Engine.engine().importPackage(value);
        return node;
    }

}
