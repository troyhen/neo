package org.neo.parse;

import java.util.List;

/**
 * Wrapper for rules which must match one or more times.
 */
class RulePlus extends RuleStar {

    RulePlus(OptimizedRule child) {
        super(child);
    }

    @Override
    public Progress explore(Progress production, boolean ignore) {
        Progress after = child.explore(production, ignore);
        return super.explore(after, ignore);
    }

    @Override
    @Deprecated
    public boolean findStarts(List<String> list) {
        return child.findStarts(list);
    }

    @Override
    public Node match(Node node, List<Node.Match> matched) {
        node = child.match(node, matched);
        if (node == null) return null;
        return super.match(node, matched);
    }

    @Override
    public String toString() {
        return child.toString() + '+';
    }

}
