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

    @Override
    public boolean findStarts(Set<String> list) {
        return true;
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
    }

    @Override
    public String toString() {
        return super.toString() + " < ";
    }

}
