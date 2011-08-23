package org.dia.parse;

import java.util.List;
import java.util.Stack;
import org.dia.Node;

class RuleOpt implements Rule {
    private Rule child;

    RuleOpt(Rule child) {
        this.child = child;
    }

    @Override
    public int complexity() {
        return child.complexity();
    }

    @Override
    public int match(Stack<Node> stack, int start) {
//    public int match(List<Node> stack, int start, Byte[] found) {
        int index = child.match(stack, start);//, found);
        if (index >= 0) return index;
        return start;
    }

}
