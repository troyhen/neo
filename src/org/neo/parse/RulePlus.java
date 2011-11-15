package org.neo.parse;

import java.util.List;
import java.util.Set;

/**
 * Wrapper for rules which must match one or more times.
 */
class RulePlus extends RuleStar {

    RulePlus(OptimizedRule child) {
        super(child);
    }

//    @Override
//    public Progress explore(Progress production, boolean ignore) {
//        Progress after = child.explore(production, ignore);
//        return super.explore(after, ignore);
//    }

    @Override
    public boolean findStarts(Set<String> list) {
        return child.findStarts(list);
    }

    @Override
    public Node parse(Node from, List<Node.Match> matched) {
        int size = matched.size();
        try {
            from = child.parse(from, matched);
        } catch (Mismatch e) {
            Node.revert(matched, size);
            throw e;
        }
        return super.parse(from, matched);
    }

    @Override
    public String toString() {
        return child.toString() + '+';
    }

}
