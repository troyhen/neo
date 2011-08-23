package org.dia.parse;

import java.util.List;
import java.util.Stack;
import org.dia.Node;

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

}
