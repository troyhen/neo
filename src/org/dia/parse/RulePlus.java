package org.dia.parse;

import java.util.List;
import java.util.Stack;
import org.dia.Node;

class RulePlus extends RuleStar {

    RulePlus(Rule child) {
        super(child);
    }

    @Override
    public int match(Stack<Node> stack, int start) {
//    public int match(List<Node> stack, int start, Byte[] found) {
        int index = child.match(stack, start);//, found);
        if (index < 0) return index;
        return super.match(stack, index);//, found);
    }

}
