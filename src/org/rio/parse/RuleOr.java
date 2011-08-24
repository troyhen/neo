package org.rio.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.rio.Node;

class RuleOr implements Rule {
    
    private List<Rule> rules = new ArrayList<Rule>();

    RuleOr(List<List<Rule>> lists) {
        for (List<Rule> list : lists) {
            rules.add(new RuleGroup(list));
        }
    }

    @Override
    public int complexity() {
        int result = 0;
        for (Rule rule : rules) {
            return Math.max(rule.complexity(), result);
        }
        return result;
    }

    @Override
    public Node match(Node start, List<Node> matched) {
        for (Rule rule : rules) {
            int size = matched.size();
            Node node = rule.match(start, matched);
            if (node != null) return node;
            Node.revert(matched, size);
        }
        return null;
    }

}
