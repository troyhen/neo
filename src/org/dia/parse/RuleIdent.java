package org.dia.parse;

import java.util.List;
import java.util.Stack;
import org.dia.Node;

class RuleIdent implements Rule {
    
    private final String ident;
    private final String identBase;
    private final String identPlus;

    RuleIdent(String id) {
        this.ident = id;
        this.identBase = id.startsWith("^") || id.startsWith("!") || id.startsWith("@") ? id.substring(1) : id;
        this.identPlus = this.identBase + '.';
    }

    @Override
    public int complexity() {
        return 1;
    }

//    @Override
//    public int match(Stack<Node> stack, int start) {
////    public int match(List<Node> stack, int start, Byte[] found) {
//        if (start >= stack.size()) return -1;
//        final String name = stack.get(start).getName();
//        if (name.equals(identBase) || name.startsWith(identPlus)) {
////            found[start] = 1;
//            return start + 1;
//        }
//        return -1;
//    }

    @Override
    public Node match(Node node, List<Node> matched) {
        final String name = node.getName();
        if (name.equals(identBase) || name.startsWith(identPlus)) {
            Node next = node.getNext();
            matched.add(node);
            if (next == null) return node.getParent();
            return next;
        }
        return null;
    }

//    @Override
//    public void reduce(Node node, Node newParent) {
//        newParent.add(node);
//    }

}
