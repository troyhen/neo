package org.rio.parse;

import java.util.List;
import org.rio.Node;

class RuleIdent implements Rule {
    
//    private final String ident;
    private final String identBase;
    private final String identDot;
    private final int flags;

    RuleIdent(String ident) {
//        this.ident = ident;
        this.flags = (ident.startsWith("^") ? Node.ROOT : 0) +
                (ident.startsWith("!") ? Node.IGNORE : 0) +
                (ident.startsWith("@") ? Node.SUBSUME : 0);
        this.identBase = flags != 0 ? ident.substring(1) : ident;
        this.identDot = this.identBase + '.';
    }

    @Override
    public Node match(Node node, List<Node> matched) {
        final String name = node.getName();
        if (name.equals(identBase) || name.startsWith(identDot)) {
            Node next = node.getNext();
            matched.add(node);
            node.setFlags((byte) flags);
            if (next == null) return node.getParent();
            return next;
        }
        return null;
    }

}
