package org.neo.core;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.neo.Log;
import org.neo.NeoException;
import org.neo.Node;
import org.neo.PluginBase;
import org.neo.back.Backend;
import org.neo.back.Render;

/**
 *
 * @author Troy Heninger
 */
public class CorePlugin extends PluginBase {

    private static String capitalize(String word) {
        if (word == null || word.length() == 0) return word;
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }

    @Override
    public void render(Node node, String backend) {
        try {
            Class<Backend> type = findClass(node, backend);
            Backend render = type.newInstance();
            render.render(node);
        } catch (Exception ex) {
            Log.logger.log(Level.SEVERE, ex.toString(), ex);
            throw new NeoException(ex);
        }
    }

    private static Class<Backend> findClass(Node node, String backend) throws ClassNotFoundException {
        String nodeName = node.getName();
        String nodeVar = "";
        int dot = nodeName.indexOf('.');
        if (dot > 0) {
            nodeVar = nodeName.substring(dot + 1);
            nodeName = nodeName.substring(0, dot);
        }
        String className = "org.neo.back." + capitalize(backend) + capitalize(nodeName) + capitalize(nodeVar);
        Class<Backend> type;
        try {
            type = (Class<Backend>) Class.forName(className);
        } catch (ClassNotFoundException ex) {
            className = "org.neo.back." + capitalize(backend) + capitalize(nodeName);
            type = (Class<Backend>) Class.forName(className);
        }
        return type;
    }

}
