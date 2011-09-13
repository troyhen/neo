package org.neo.parse;

import java.util.List;
import org.neo.Node;

class RulePlus extends RuleStar {

    RulePlus(Rule child) {
        super(child);
    }

    @Override
    public Node match(Node node, List<Node> matched) {
        node = child.match(node, matched);
        if (node == null) return null;
        return super.match(node, matched);
    }

    @Override
    public String toString() {
        return child.toString() + '+';
    }

}
