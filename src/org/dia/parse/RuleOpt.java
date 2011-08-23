package org.dia.parse;

import java.util.List;
import org.dia.Node;

class RuleOpt implements Rule {
    private Rule child;

    RuleOpt(Rule child) {
        this.child = child;
    }

    @Override
    public int complexity() {
        return child.complexity();
    }

    @Override
    public Node match(Node node, List<Node> matched) {
        Node next = child.match(node, matched);
        if (next != null) return next;
        return node;
    }

}
