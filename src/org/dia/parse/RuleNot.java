package org.dia.parse;

import java.util.List;
import java.util.Stack;
import org.dia.Node;

class RuleNot implements Rule {
    
    private Rule child;

    RuleNot(Rule child) {
        this.child = child;
    }

    @Override
    public int complexity() {
        return child.complexity();
    }

//    @Override
//    public int match(Stack<Node> stack, int start) {
////    public int match(List<Node> stack, int start, Byte[] found) {
//        int index = child.match(stack, start);//, found);
//        if (index >= 0) {
////            found[start] = 1;
//            return -1;
//        }
//        return start;
//    }

    @Override
    public Node match(Node node, List<Node> matched) {
        int size = matched.size();
        Node result = child.match(node, null);
        Node.revert(matched, size);
        if (result != null) return null;
        return node;
    }

//    @Override
//    public void reduce(Node node, Node newParent) {
//    }

}
