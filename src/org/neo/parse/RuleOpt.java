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

//    @Override
//    public Progress explore(Progress progress, boolean ignore) {
//        Progress after = child.explore(progress, ignore);
//        after.getState().setGoto(progress.getState());  // TODO this allows loops which is not correct
//        return after;
//    }

    @Override
    @Deprecated
    public boolean findStarts(List<String> list) {
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
        Node next = child.match(from, matched);
        return next != null ? next : from;
    }

    @Override
    public String toString() {
        return child.toString() + '?';
    }

}
