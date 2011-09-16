package org.neo.parse;

import java.util.List;
import org.neo.Node;

class RuleGroup implements Rule {
    
    protected List<Rule> rules;

    public RuleGroup(List<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public Node match(Node node, List<Node> matched) {
        for (Rule rule : rules) {
            node = rule.match(node, matched);
            if (node == null) break;
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
