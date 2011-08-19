package org.dia.parse;

import java.util.Stack;
import org.dia.Node;

interface Rule {
//    public Node match(boolean first);
    int complexity();
    int match(Stack<Node> stack, int start);
}
