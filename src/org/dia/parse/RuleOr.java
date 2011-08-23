package org.dia.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.dia.Node;

class RuleOr implements Rule {
    
    private List<Rule> rules = new ArrayList<Rule>();

    RuleOr(List<List<Rule>> lists) {
        for (List<Rule> list : lists) {
            rules.add(new RuleGroup(list));
        }
    }

    @Override
    public int complexity() {
        int result = 0;
        for (Rule rule : rules) {
            return Math.max(rule.complexity(), result);
        }
        return result;
    }

//    @Override
//    public int match(Stack<Node> stack, int start) {
////    public int match(List<Node> stack, int start, Byte[] found) {
//        for (Rule rule : rules) {
//            int index = rule.match(stack, start);//, found);
//            if (index >= 0) return index;
//        }
//        return -1;
//    }

    @Override
    public Node match(Node start, List<Node> matched) {
        for (Rule rule : rules) {
            int size = matched.size();
            Node node = rule.match(start, matched);
            if (node != null) return node;
            Node.revert(matched, size);
        }
        return null;
    }

//    @Override
//    public void reduce(Node start, Node newParent) {
//        for (Rule rule : rules) {
//            Node node = rule.match(start);
//            if (node != null) {
//                rule.reduce(node, newParent);
//            }
//        }
//    }

}
