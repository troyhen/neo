package org.neo.parse;

import java.util.List;
import java.util.Set;

/**
 * Wrapper for rules which can be matched any number of times.
 */
class RuleStar implements OptimizedRule {

    protected final OptimizedRule child;

    RuleStar(OptimizedRule child) {
        this.child = child;
    }

//    @Override
//    public Progress explore(Progress progress, boolean ignore) {
//        Progress after = child.explore(progress, ignore);
//        after.getState().setGoto(progress.getState());
//        return after;
//    }

    @Override
    public boolean findStarts(Set<String> list) {
        child.findStarts(list);
        return true;
    }

    @Override
    public Node parse(Node from, List<Node.Match> matched) {
        for (;;) {
//            if (from.getParent() == null) break;
            try {
                boolean last = from.getNext() == null;
                Node next = child.parse(from, matched);
                from = next;
                if (last) {
                    return from;
                }
            } catch (Mismatch e) {
                return from;
            }
        }
    }

    @Override
    public String toString() {
        return child.toString() + '*';
    }
    
}
