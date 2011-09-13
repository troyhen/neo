package org.neo.parse;

import java.util.ArrayList;
import java.util.List;
import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
class RuleBefore extends RuleGroup {

    public RuleBefore(List<Rule> rules) {
        super(rules);
    }

    @Override
    public Node match(Node start, List<Node> matched) {
        Node node = start;
        for (int ix = rules.size() - 1; ix >= 0; ix--) {
            Rule rule = rules.get(ix);
            Node prev = node.getPrev();
            if (prev == null) {
                if (rule instanceof RuleNot) continue;
                return null;
            }
            int size = matched.size();
            Node found = rule.match(prev, matched);
            if (found == null) return null;
            Node.revert(matched, size);
            if (found != prev) node = prev;
        }
        return start;
    }

    @Override
    public String toString() {
        return super.toString() + " < ";
    }

}