package org.dia.parse;

import java.util.List;
import java.util.Stack;
import org.dia.Node;

class RulePeek implements Rule {
    
    protected final List<Rule> rules;

    RulePeek(List<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public int complexity() {
        int result = 0;
        for (Rule rule : rules) {
            result += rule.complexity();
        }
        return result;
    }

    @Override
    public int match(Stack<Node> stack, int start) {
//    public int match(List<Node> stack, int start, Byte[] found) {
        int index = start;
        for (Rule rule : rules) {
            index = rule.match(stack, index);//, found);
            if (index < 0) return index;
        }
        return start;
    }

}
