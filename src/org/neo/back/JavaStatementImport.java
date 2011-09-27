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
        String path = node.getValue().toString();
        buff.append("import ");
//        try {
//            if (isStatic(path)) {
//                buff.append("static ");
//            }
//        } catch (ClassNotFoundException ex) {
//            throw new NeoException(ex);
//        }
        buff.append(path).append(".*;").eol();
        buff.append("import static ").append(path).append(".N.*;").eol();
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
