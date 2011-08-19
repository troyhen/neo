package org.dia.parse;

import java.util.Stack;
import org.dia.Compiler;
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
//    public Node match(boolean first) {
//        Node node = null;
//        int offset = Compiler.offset();
//        try {
//            node = child.match(first);
//        } catch (Missing e) {
//            Compiler.offset(offset);
//        }
//        return node;
//    }

    @Override
    public int match(Stack<Node> stack, int start) {
        int index = child.match(stack, start);
        if (index >= 0) return -1;
        return start;
    }
}
