package org.dia.parse;

import java.util.Stack;
import org.dia.Compiler;
import org.dia.Node;

class RuleStar implements Rule {
    protected final Rule child;

    RuleStar(Rule child) {
        this.child = child;
    }

//    @Override
//    public Node match(boolean first) {
//        Node firstNode = null;
//        int offset = Compiler.offset();
//        try {
//            for (;;) {
//                Node nextNode = child.match(first);
//                if (firstNode == null) firstNode = nextNode;
//                else if (nextNode != null) firstNode.append(nextNode);
//                first = false;
//                offset = Compiler.offset();
//            }
//        } catch (Missing e) {
//            Compiler.offset(offset);
//        }
//        return firstNode;
//    }

    @Override
    public int match(Stack<Node> stack, int start) {
        int index;
        do {
            index = child.match(stack, start);
            if (index >= 0) start = index;
        } while (index >= 0);
        return start;
    }
}
