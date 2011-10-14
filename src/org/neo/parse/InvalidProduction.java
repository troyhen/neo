package org.neo.parse;

import java.util.List;
import org.neo.parse.Node.Match;
import org.neo.Plugin;

/**
 *
 * @author Troy Heninger
 */
public class InvalidProduction extends Production {

    public InvalidProduction(Plugin plugin, String message, String definition) {
        super(plugin, message, definition);
    }

    @Override
    public Node match(Node node, List<Match> matched) {
        Node found = super.match(node, matched);
        if (found != null) throw new ParseException(name + " at line " + node.getLine());
        return null;
    }

}
