package org.neo.parse;

import java.util.ArrayList;
import java.util.List;

class RuleOr implements OptimizedRule {
    
    private List<OptimizedRule> rules = new ArrayList<OptimizedRule>();

    public RuleOr(List<List<OptimizedRule>> lists) {
        for (List<OptimizedRule> list : lists) {
            if (list.size() == 1) rules.add(list.get(0));
            else rules.add(new RuleGroup(list));
        }
    }

    @Override
    public Progress explore(Progress progress, boolean ignore) {
        State startState = progress.getState();
        State endState = null;
        Progress before = progress;
        Progress after = null;
        for (OptimizedRule rule : rules) {
            if (after != null) {
                before = after.getNext();
                before.setState(startState);
            }
            after = rule.explore(before, ignore);
            if (endState == null)
                endState = after.getState();
            else
                endState.merge(after.getState());
        }
        return after;
    }

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
