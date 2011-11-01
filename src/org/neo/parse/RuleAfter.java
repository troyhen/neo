package org.neo.parse;

import java.util.List;

class RuleAfter extends RuleGroup {
    
    RuleAfter(List<OptimizedRule> rules) {
        super(rules);
    }

//    @Override
//    public Progress explore(Progress progress, boolean ignore) {
//        return super.explore(progress, true);
//    }

    @Override
    public boolean findStarts(List<String> list) {
        return false;
    }

    @Override
    public Node match(Node node, List<Node.Match> matched) {
        int size = matched.size();
        Node next = super.match(node, matched);
        Node.revert(matched, size);
        if (next == null) return null;
        return node;
    }

    @Override
    public Node parse(Node from, List<Node.Match> matched) {
        int size = matched.size();
        Node next = super.parse(from, matched);
        Node.revert(matched, size);
        if (next == null) return null;
        return from;
    }

    @Override
    public String toString() {
        return " > " + super.toString();
    }

}
