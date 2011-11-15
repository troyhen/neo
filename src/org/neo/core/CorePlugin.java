package org.neo.core;

import neo.lang.N;
import org.neo.util.Log;
import org.neo.NeoException;
import org.neo.parse.Node;
import org.neo.PluginBase;
import org.neo.back.Backend;
import org.neo.parse.Engine;

/**
 *
 * @author Troy Heninger
 */
public class CorePlugin extends PluginBase {

//    @SuppressWarnings("Unchecked")
    private static Class<Backend> findClass(Node node, String backend) throws ClassNotFoundException {
        String nodeName = node.getName();
        String nodeVar = "";
        int dot = nodeName.indexOf('_');
        if (dot > 0) {
            nodeVar = nodeName.substring(dot + 1);
            nodeName = nodeName.substring(0, dot);
        }
        String className = "org.neo.back." + N.capitalize(backend) + N.capitalize(nodeName) + N.capitalize(nodeVar);
        Class<Backend> type;
        try {
            type = (Class<Backend>) Engine.engine().loadClass(className);
        } catch (ClassNotFoundException ex) {
            className = "org.neo.back." + N.capitalize(backend) + N.capitalize(nodeName);
            type = (Class<Backend>) Engine.engine().loadClass(className);
        }
        return type;
    }

    @Override
    public void render(Node node, String backend) {
        try {
            Class<Backend> type = findClass(node, backend);
            Backend render = type.newInstance();
            render.render(node);
        } catch (NeoException e) {
            throw e;
        } catch (Exception ex) {
            while (ex.getCause() != null) ex = (Exception) ex.getCause();
            Log.error(ex);
            throw new NeoException(ex);
        }
    }

}
