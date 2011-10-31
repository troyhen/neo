package org.neo.parse;

import java.util.List;

/**
 * Rule wrapper for an optional element or group. Parse is successful whether or not the enclosed rules are matched.
 * @author Troy Heninger
 */
class RuleOpt implements OptimizedRule {
    private OptimizedRule child;

    RuleOpt(OptimizedRule child) {
        this.child = child;
    }

    @Override
    public Progress explore(Progress progress, boolean ignore) {
        Progress after = child.explore(progress, ignore);
        progress.getState().link("*", after.getState());    // if nothing matches move on (and don't reduce)
        return after;
    }

    @Override
    @Deprecated
    public boolean findStarts(List<String> list) {
        child.findStarts(list);
        return true;
    }

    @Override
    public Node match(Node node, List<Node.Match> matched) {
        Node next = child.match(node, matched);
        if (next != null) return next;
        return node;
    }

    @Override
    public String toString() {
        return child.toString() + '?';
    }

}
