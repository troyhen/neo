package org.neo.parse;

import java.util.List;
import java.util.Set;

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
    public boolean findStarts(Set<String> list) {
        return true;
    }

    @Override
    public Node parse(Node from, List<Node.Match> matched) {
        int size = matched.size();
        try {
            child.parse(from, matched);
        } catch (Mismatch e) {
            return from;
        } finally {
            Node.revert(matched, size);
        }
        throw new Mismatch(from, toString());
    }

    @Override
    public String toString() {
        return child.toString() + '-';
    }

}
