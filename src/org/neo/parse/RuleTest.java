package org.neo.parse;

import java.util.List;
import java.util.Set;

/**
 * A test rule wrapper. Parse succeeds if enclosed rules are matched and fails otherwise.
 * In either case no nodes are consumed/reduced.
 * @author Troy Heninger
 */
class RuleTest implements OptimizedRule {

    private OptimizedRule child;

    RuleTest(OptimizedRule child) {
        this.child = child;
    }

//    @Override
//    public Progress explore(Progress progress, boolean ignore) {
//        Progress after = child.explore(progress, !ignore);
//        after.getState().setDeadEnd(true);
//        return progress;
//    }

    @Override
    public boolean findStarts(Set<String> list) {
        return true;
    }

    @Override
    public Node match(Node node, List<Node.Match> matched) {
        int size = matched.size();
        Node result = child.match(node, matched);
        Node.revert(matched, size);
        if (result == null) return null;
        return node;
    }

    @Override
    public Node parse(Node from, List<Node.Match> matched) {
        int size = matched.size();
        try {
            child.parse(from, matched);
        } finally {
            Node.revert(matched, size);
        }
        return from;
    }

    @Override
    public String toString() {
        return child.toString() + '-';
    }

}
