package org.neo.parse;

import java.util.List;
import java.util.Set;

/**
 *
 * @author Troy Heninger
 */
class RuleBefore extends RuleGroup {

    public static ThreadLocal<Boolean> simpleCheck = new ThreadLocal<Boolean>();

    public RuleBefore(List<OptimizedRule> rules) {
        super(rules);
        simpleCheck.set(false);
    }

//    @Override
//    public Progress explore(Progress progress, boolean ignore) {
//        return progress;
//    }

    @Override
    public boolean findStarts(Set<String> list) {
        return true;
//        boolean more = super.findStarts(list);
//        if (more) {
//            list.clear();
//            list.add("*");
//        }
//        return false;
    }

    @Override
    public Node match(Node start, List<Node.Match> matched) {
//        Node node = start;
        Node found = super.match(start, matched);
        if (found == null)
            return null;
        Node.revert(matched, 0);
        if (found == start) {
            found = found.getNextOrLast(); // ensure next rule matches the following node
        }
        return found;
//        for (int ix = rules.size() - 1; ix >= 0; ix--) {
//            Rule rule = rules.get(ix);
//            Node prev = node.getPrev();
//            if (prev == null) {
//                if (rule instanceof RuleNot) continue;
//                return null;
//            }
//            int size = matched.size();
//            Node found = rule.match(prev, matched);
//            if (found == null) return null;
//            Node.revert(matched, size);
//            if (found != prev) node = prev;
//        }
//        return start;
    }

    @Override
    public Node parse(Node from, List<Node.Match> matched) {
        Node node = from.getPrevOrFirst();
        simpleCheck.set(true);
        try {
            super.parse(node, matched);
            return from;
        } finally {
            simpleCheck.set(false);
            Node.revert(matched, 0);
        }
//        if (found == node) {
//            found = found.getNext(); // ensure next rule matches the following node
//        }
//        return found;
    }

    @Override
    public String toString() {
        return super.toString() + " < ";
    }

}
