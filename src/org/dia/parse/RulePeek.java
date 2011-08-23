package org.dia.parse;

import java.util.List;
import org.dia.Node;

class RulePeek extends RuleGroup {
    
    RulePeek(List<Rule> rules) {
        super(rules);
    }

    @Override
    public Node match(Node node, List<Node> matched) {
        int size = matched.size();
        Node next = super.match(node, matched);
        Node.revert(matched, size);
        if (next == null) return null;
        return node;
    }

}
