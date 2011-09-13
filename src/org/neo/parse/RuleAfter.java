package org.neo.parse;

import java.util.List;
import org.neo.Node;

class RuleAfter extends RuleGroup {
    
    RuleAfter(List<Rule> rules) {
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

    @Override
    public String toString() {
        return " > " + super.toString();
    }

}
