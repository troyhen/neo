package org.dia.parse;

import java.util.List;
//import java.util.Stack;
import org.dia.Node;

interface Rule {
//    public Node match(boolean first);
    int complexity();
//    int match(List<Node> stack, int start);//, Byte[] found);
//    int match(Stack<Node> stack, int start);
    Node match(Node node, List<Node> matched);
//    void reduce(Node node, Node newParent);
}
