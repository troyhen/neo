package org.dia.parse;

import java.util.List;
import java.util.Stack;
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

//    @Override
//    public int match(Stack<Node> stack, int start) {
////    public int match(List<Node> stack, int start, Byte[] found) {
//        int index;
//        do {
//            index = child.match(stack, start);//, found);
//            if (index >= 0) start = index;
//        } while (index >= 0);
//        return start;
//    }

    @Override
    public Node match(Node node, List<Node> matched) {
        for (;;) {
            Node next = child.match(node, matched);
            if (next == null) break;
            node = next;
        }
        return node;
    }

//    @Override
//    public void reduce(Node node, Node newParent) {
//        for (;;) {
//            Node next = child.match(node);
//            if (next == null) break;
//            child.reduce(node, newParent);
//            node = next;
//        }
//    }
    
}
