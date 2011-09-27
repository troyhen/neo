package org.neo.parse;

import java.util.List;
import org.neo.Node;

class RuleIdent implements Rule {
    
//    private final String ident;
    private final String identBase;
    private final String identDot;
    private final byte flags;

    RuleIdent(String ident) {
//        this.ident = ident;
        this.flags = (byte) ((ident.startsWith("^") ? Node.ROOT : 0) +
                (ident.startsWith("!") ? Node.IGNORE : 0) +
                (ident.startsWith("@") ? Node.SUBSUME : 0));
        this.identBase = flags != 0 ? ident.substring(1) : ident;
        this.identDot = this.identBase + '_';
    }

    @Override
    public Node match(Node node, List<Node.Match> matched) {
        final String name = node.getName();
        if (name.equals(identBase) || name.startsWith(identDot)) {
            Node next = node.getNext();
            matched.add(node.newMatch(flags));
//            node.setFlags((byte) flags);    //TODO problem because a higher rule may cancel this match
            if (next == null) return node.getParent();
            return next;
        }
        return null;
    }

    @Override
    public String toString() {
        return identBase;
    }

}
