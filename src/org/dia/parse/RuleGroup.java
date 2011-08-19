package org.dia.parse;

import java.util.List;
import java.util.Stack;
import org.dia.Node;

class RuleGroup implements Rule {
    protected final List<Rule> rules;

    RuleGroup(List<Rule> rules) {
        this.rules = rules;
    }

//    @Override
//    public Node match(boolean first) {
//        Node firstNode = null;
//        for (Rule rule : rules) {
//            Node nextNode = rule.match(first);
//            if (firstNode == null) firstNode = nextNode;
//            else if (nextNode != null) firstNode.append(nextNode);
//            first = false;
//        }
//        return firstNode;
//    }

    @Override
    public int match(Stack<Node> stack, int start) {
        int index = start;
        for (Rule rule : rules) {
            index = rule.match(stack, index);
            if (index < 0) return index;
        }
        return index;
    }

//    public Node matched(Node node) {
//        return node;
//    }
}
