package org.rio.parse;

import java.util.List;
import org.rio.Node;

class RuleGroup implements Rule {
    
    protected List<Rule> rules;

    RuleGroup(List<Rule> rules) {
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

}
