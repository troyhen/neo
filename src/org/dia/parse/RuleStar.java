package org.dia.parse;

import java.util.List;
import org.dia.Node;

class RuleStar implements Rule {

    protected final Rule child;

    RuleStar(Rule child) {
        this.child = child;
    }

    @Override
    public int complexity() {
        return child.complexity();
    }

    @Override
    public Node match(Node node, List<Node> matched) {
        for (;;) {
            Node next = child.match(node, matched);
            if (next == null) break;
            node = next;
        }
        return node;
    }
    
}
