package org.rio.parse;

import java.util.List;
import org.rio.Node;

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

}
