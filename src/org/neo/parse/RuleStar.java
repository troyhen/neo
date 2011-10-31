package org.neo.parse;

import java.util.List;

/**
 * Wrapper for rules which can be matched any number of times.
 */
class RuleStar implements OptimizedRule {

    protected final OptimizedRule child;

    RuleStar(OptimizedRule child) {
        this.child = child;
    }

    @Override
    public Progress explore(Progress progress, boolean ignore) {
        Progress after = child.explore(progress, ignore);
        Progress done = after.getNext();
        progress.getState().merge(after.getState());
        progress.getState().link("*", done.getState());    // if nothing matches move on (and don't reduce)
        return done;
    }

    @Override
    @Deprecated
    public boolean findStarts(List<String> list) {
        child.findStarts(list);
        return true;
    }

    @Override
    public Node match(Node node, List<Node.Match> matched) {
        for (;;) {
            Node next = child.match(node, matched);
            if (next == null) break;
            node = next;
        }
        return node;
    }

    @Override
    public String toString() {
        return child.toString() + '*';
    }
    
}
