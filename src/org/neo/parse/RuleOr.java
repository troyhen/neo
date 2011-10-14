package org.neo.parse;

import java.util.ArrayList;
import java.util.List;

class RuleOr implements Rule {
    
    private List<Rule> rules = new ArrayList<Rule>();

    public RuleOr(List<List<Rule>> lists) {
        for (List<Rule> list : lists) {
            rules.add(new RuleGroup(list));
        }
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
