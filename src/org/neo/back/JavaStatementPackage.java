package org.neo.back;

import org.neo.parse.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaStatementPackage implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output(JavaCompilation.OUTSIDE);
        String path = node.getValue().toString();
        buff.append("package ").append(path).append(';').eol();
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
