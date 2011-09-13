package org.neo.parse;

import java.util.List;
import org.neo.Node;

class RuleOpt implements Rule {
    private Rule child;

    RuleOpt(Rule child) {
        this.child = child;
    }

    @Override
    public Node match(Node node, List<Node> matched) {
        Node next = child.match(node, matched);
        if (next != null) return next;
        return node;
    }

    @Override
    public String toString() {
        return child.toString() + '?';
    }

}
