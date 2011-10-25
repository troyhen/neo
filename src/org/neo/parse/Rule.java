package org.neo.parse;

import java.util.List;

/**
 * Element of a grammar production which evaluates incoming tokens for a match.
 */
public interface Rule {

    /**
     * Attempt to match the given token node.
     * @param node token node to match
     * @param matched list of matched nodes (used for reduction)
     * @return the following node or null if no match
     */
    Node match(Node node, List<Node.Match> matched);

}

