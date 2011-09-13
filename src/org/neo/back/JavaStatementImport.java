package org.neo.back;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaStatementImport implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output(0);
        String path = getPath(node.getFirst());
        buff.append("import ");
//        try {
//            if (isStatic(path)) {
//                buff.append("static ");
//            }
//        } catch (ClassNotFoundException ex) {
//            throw new NeoException(ex);
//        }
        buff.append(path);
        buff.append(".*");
        buff.append(";").eol();
        buff.append("import static ");
        buff.append(path);
        buff.append(".N.*");
        buff.append(";").eol();
    }

    public static String getPath(Node node) {
        StringBuilder buff = new StringBuilder();
        while (node != null) {
            buff.append(node.getValue());
            node = node.getNext();
        }
        return buff.toString();
    }

//    private boolean isStatic(String path) throws ClassNotFoundException {
//        String pkg = path;
//        if (pkg.endsWith(".*")) pkg = pkg.substring(0, pkg.length() - 2);
//        try {
//            Class.forName(pkg);
//            return false;
//        } catch (ClassNotFoundException e) {}
//        int ix = pkg.lastIndexOf(".");
//        if (ix < 0) return false;
//        pkg = pkg.substring(0, ix);
//        Class.forName(pkg);
//        return true;
////        Package.getPackage(pkg);
//    }

 }
