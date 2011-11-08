package org.neo.parse;

import java.util.List;
import java.util.Set;

/**
 * Rule wrapper for an optional element or group. Parse is successful whether or not the enclosed rules are matched.
 * @author Troy Heninger
 */
class RuleOpt implements OptimizedRule {
    private OptimizedRule child;

    RuleOpt(OptimizedRule child) {
        this.child = child;
    }

//    @Override
//    public Progress explore(Progress progress, boolean ignore) {
//        Progress after = child.explore(progress, ignore);
//        after.getState().setGoto(progress.getState());  // TODO this allows loops which is not correct
//        return after;
//    }

    @Override
    @Deprecated
    public boolean findStarts(Set<String> list) {
        child.findStarts(list);
        return true;
    }

    @Override
    public Node match(Node node, List<Node.Match> matched) {
        Node next = child.match(node, matched);
        return next != null ? next : node;
    }

    @Override
    public Node parse(Node from, List<Node.Match> matched) {
//        if (from.getParent() == null) return from;
        try {
            Node next = child.parse(from, matched);
            return next;
        } catch (Mismatch e) {
            return from;
        }
    }

    @Override
    public String toString() {
        return child.toString() + '?';
    }

}
