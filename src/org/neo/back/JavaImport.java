package org.neo.back;

import org.neo.Node;
import org.neo.NeoException;

/**
 *
 * @author Troy Heninger
 */
public class JavaImport implements Render {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output();
        buff.append("import ");
        String path = getPath(node);
        try {
            if (isStatic(path)) {
                buff.append("static ");
            }
        } catch (ClassNotFoundException ex) {
            throw new NeoException(ex);
        }
        buff.append(path);
        buff.append(";").eol();
    }

    private String getPath(Node node) {
        StringBuilder buff = new StringBuilder();
        node = node.getFirst();
        while (node != null) {
            buff.append(node.getValue());
            node = node.getNext();
        }
        return buff.toString();
    }

    private boolean isStatic(String path) throws ClassNotFoundException {
        String pkg = path;
        if (pkg.endsWith(".*")) pkg = pkg.substring(0, pkg.length() - 2);
        try {
            Class.forName(pkg);
            return false;
        } catch (ClassNotFoundException e) {}
        int ix = pkg.lastIndexOf(".");
        if (ix < 0) return false;
        pkg = pkg.substring(0, ix);
        Class.forName(pkg);
        return true;
//        Package.getPackage(pkg);
    }

 }
