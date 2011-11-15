package org.neo.parse;

import java.util.List;
import org.neo.parse.Node.Match;
import org.neo.Plugin;

/**
 *
 * @author Troy Heninger
 */
public class InvalidProduction extends Production {

    private String error;

    public InvalidProduction(Plugin plugin, String name, String definition, String message) {
        super(plugin, name, definition);
        error = message;
    }

    @Override
    public Node parse(Node from, List<Match> matched) {
        Node next = super.parse(from, matched);
        if (error != null) throw new ParseException(error, from);
        return next;
    }
}
