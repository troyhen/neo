package org.neo.parse;

import java.util.List;

/**
 * A negative rule wrapper. Parse fails if enclosed rules are matched and succeeds otherwise.
 * @author Troy Heninger
 */
class RuleNot implements OptimizedRule {
    
    private OptimizedRule child;

    RuleNot(OptimizedRule child) {
        this.child = child;
    }

//    @Override
//    public Progress explore(Progress progress, boolean ignore) {
//        Progress after = child.explore(progress, !ignore);
//        after.getState().setDeadEnd(true);
//        return progress;
//    }

    @Override
    public boolean findStarts(List<String> list) {
        return true;
    }

    @Override
    public Node match(Node node, List<Node.Match> matched) {
        int size = matched.size();
        Node result = child.match(node, matched);
        Node.revert(matched, size);
        if (result != null) return null;
        return node;
    }

    @Override
    public Node parse(Node from, List<Node.Match> matched) {
//        if (from.getParent() == null) return from;
        int size = matched.size();
        Node result = child.parse(from, matched);
        Node.revert(matched, size);
        if (result != null) return null;
        return from;
    }

    @Override
    public String toString() {
        return child.toString() + '-';
    }

}
