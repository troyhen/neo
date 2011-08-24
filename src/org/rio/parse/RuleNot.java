package org.rio.parse;

import java.util.List;
import java.util.Stack;
import org.rio.Node;

class RuleNot implements Rule {
    
    private Rule child;

    RuleNot(Rule child) {
        this.child = child;
    }

    @Override
    public int complexity() {
        return child.complexity();
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
