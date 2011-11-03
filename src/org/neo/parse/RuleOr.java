package org.neo.parse;

import java.util.ArrayList;
import java.util.List;

/*public*/ class RuleOr implements OptimizedRule {
    
    private List<OptimizedRule> rules = new ArrayList<OptimizedRule>();

//    public RuleOr() {
//    }

    public RuleOr(List<List<OptimizedRule>> lists) {
        for (List<OptimizedRule> list : lists) {
            if (list.size() == 1) rules.add(list.get(0));
            else rules.add(new RuleGroup(list));
        }
    }

//    public void add(OptimizedRule rule) {
//        rules.add(rule);
//    }

//    @Override
//    public Progress explore(Progress progress, boolean ignore) {
//        for (OptimizedRule rule : rules) {
//            rule.explore(progress, ignore);
//        }
//        Progress after = progress.getNext();
//        after.setState(Engine.engine().getState(after, true));
//        return after;
//    }

    @Override
    @Deprecated
    public boolean findStarts(List<String> list) {
        boolean more = false;
        for (OptimizedRule rule : rules) {
            more |= rule.findStarts(list);
        }
        return more;
    }

    @Override
    public Node match(Node start, List<Node.Match> matched) {
        for (Rule rule : rules) {
            int size = matched.size();
            Node node = rule.match(start, matched);
            if (node != null) return node;
            Node.revert(matched, size);
        }
        return null;
    }

    @Override
    public Node parse(Node from, List<Node.Match> matched) {
        Node root = from.getParent();
        for (OptimizedRule rule : rules) {
            int size = matched.size();
            Node node = rule.parse(from, matched);
            if (node != null) return node;
            Node.revert(matched, size);
            while (from.getParent() != root) {
                from = from.getParent();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder("(");
        String space = "";
        for (Rule rule : rules) {
            buff.append(space);
            space = " | ";
            buff.append(rule);
        }
        buff.append(")");
        return buff.toString();
    }
}
