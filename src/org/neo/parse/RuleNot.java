package org.neo.parse;

import java.util.List;
import java.util.Stack;
import org.neo.Node;

class RuleNot implements Rule {
    
    private Rule child;

    RuleNot(Rule child) {
        this.child = child;
    }

    @Override
    public Node match(Node node, List<Node> matched) {
        int size = matched.size();
        Node result = child.match(node, null);
        Node.revert(matched, size);
        if (result != null) return null;
        return node;
    }

}