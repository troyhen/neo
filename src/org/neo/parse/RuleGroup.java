package org.neo.parse;

import java.util.List;

class RuleGroup implements Rule {
    
    protected List<Rule> rules;

    public RuleGroup(List<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public Node match(Node node, List<Node.Match> matched) {
        int size = matched.size();
        for (Rule rule : rules) {
            node = rule.match(node, matched);
            if (node == null) {
                Node.revert(matched, size);
                break;
            }
        }
        return node;
    }

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder("(");
        String space = "";
        for (Rule rule : rules) {
            buff.append(space);
            space = " ";
            buff.append(rule);
        }
        buff.append(")");
        return buff.toString();
    }

}
