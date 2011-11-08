package org.neo.parse;

import java.util.List;
import java.util.Set;

/**
 * A rule which knows how to execute much faster.
 */
public interface OptimizedRule extends Rule {

    /**
     * Used during grammar analysis to verify the grammar and prepare parse tables/structures.
     * @param progress current point within a production
     * @param ignore false if node should be reduced, true if not
     * @return following point in in the production
     */
//    Progress explore(Progress progress, boolean ignore);

    /**
     * Collects a list of starting production names.
     * @param list the list of names being collected
     * @return true if the next rule should also be checked
     */
    boolean findStarts(Set<String> list);

    /**
     * Parse from the given node.
     * @param from the node to start on
     * @param matched list of matched nodes (used for reduction)
     * @return new node if matched or null if not
     */
    public Node parse(Node from, List<Node.Match> matched);

}

