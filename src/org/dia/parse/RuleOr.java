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

    @Override
    public int match(Stack<Node> stack, int start) {
//    public int match(List<Node> stack, int start, Byte[] found) {
        for (Rule rule : rules) {
            int index = rule.match(stack, start);//, found);
            if (index >= 0) return index;
        }
        return -1;
    }

}
