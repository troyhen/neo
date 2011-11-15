package org.neo.parse;

import java.util.List;

/**
 * Element of a grammar production which evaluates incoming tokens for a match.
 */
public interface Rule {

    /**
     * Parse from the given node.
     * @param from the node to start on
     * @param matched list of matched nodes (used for reduction)
     * @return new node if matched or null if not
     */
    public Node parse(Node from, List<Node.Match> matched);
}

