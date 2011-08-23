package org.dia.parse;

import java.util.List;
import org.dia.Node;

class RuleGroup implements Rule {
    
    protected List<Rule> rules;

    RuleGroup(List<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public int complexity() {
        int result = 0;
        for (Rule rule : rules) {
            result += rule.complexity();
        }
        return result;
    }

    @Override
    public Node match(Node node, List<Node> matched) {
        for (Rule rule : rules) {
            node = rule.match(node, matched);
            if (node == null) break;
        }
        return node;
    }

}
