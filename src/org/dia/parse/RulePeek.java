package org.dia.parse;

import java.util.List;
import java.util.Stack;
import org.dia.Node;

class RulePeek extends RuleGroup {
    
    RulePeek(List<Rule> rules) {
        super(rules);
    }

    @Override
    public Node match(Node node, List<Node> matched) {
        int size = matched.size();
        Node next = super.match(node, matched);
        Node.revert(matched, size);
        if (next == null) return null;
        return node;
    }


//    @Override
//    public int match(Stack<Node> stack, int start) {
////    public int match(List<Node> stack, int start, Byte[] found) {
//        int index = start;
//        for (Rule rule : rules) {
//            index = rule.match(stack, index);//, found);
//            if (index < 0) return index;
//        }
//        return start;
//    }

//    @Override
//    public void reduce(Node node, Node newParent) {
//    }

}
