package org.dia.parse;

import java.util.Stack;
import org.dia.Node;

class RulePlus extends RuleStar {
    RulePlus(Rule child) {
        super(child);
    }

//    @Override
//    public Node match(boolean first) {
//        Node firstNode = child.match(first);
//        Node nextNode = super.match(first);
//        if (firstNode != null && nextNode != null) firstNode.append(nextNode);
//        if (firstNode == null) firstNode = nextNode;
//        return firstNode;
//    }

    @Override
    public int match(Stack<Node> stack, int start) {
        int index = child.match(stack, start);
        if (index < 0) return index;
        return super.match(stack, index);
    }
}
