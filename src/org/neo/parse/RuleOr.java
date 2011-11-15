package org.neo.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    public boolean findStarts(Set<String> list) {
        boolean more = false;
        for (OptimizedRule rule : rules) {
            more |= rule.findStarts(list);
        }
        return more;
    }

    @Override
    public Node parse(Node from, List<Node.Match> matched) {
        Node root = from.getParent();
        int size = matched.size();
        for (OptimizedRule rule : rules) {
            try {
                Node next = rule.parse(from, matched);
                return next;
            } catch (Mismatch e) {
                Node.revert(matched, size);
                while (from.getParent() != root) {
                    from = from.getParent();
                }
            }
        }
        throw new Mismatch(from, toString());
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
