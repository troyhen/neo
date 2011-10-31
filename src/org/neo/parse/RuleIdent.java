package org.neo.parse;

import java.util.List;

/**
 * Rule which matches a node by name.
 */
class RuleIdent implements OptimizedRule {

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
    public Progress explore(Progress progress, boolean ignore) {
        Progress nextStep = progress.getNext();
        if (nextStep.hasState())
            return nextStep;    // skip if already explored
        State state = progress.getState();
        state.link(identBase, nextStep.getState());
        List<Production> list = Engine.engine().findProductions(identBase);
        for (Production prod : list) {
            if (Engine.engine().hasProgress(prod, 0))
                continue;    // skip if already explored
            Progress progress1 = Engine.engine().getProgress(prod, 0, state);
            progress1.explore();
        }
        return nextStep;
    }

    @Override
    @Deprecated
    public boolean findStarts(List<String> list) {
        list.add(identBase);
        list.add(identDot);
        return false;
    }

    @Override
    public Node match(Node node, List<Node.Match> matched) {
        if (node.isNamed(identBase)) {
            Node next = node.getNext();
            matched.add(node.newMatch(flags));
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
