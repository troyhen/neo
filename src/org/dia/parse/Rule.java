package org.dia.parse;

import java.util.Stack;
import org.dia.Node;

interface Rule {
//    public Node match(boolean first);
    public int match(Stack<Node> stack, int start);
}
