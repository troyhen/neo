package org.neo.parse;

import java.util.ArrayList;
import java.util.List;

/**
 * Rule which matches a node by name.
 */
class RuleIdent implements OptimizedRule {

    private final String identBase;
    private final String identDot;
    private final byte flags;

    RuleIdent(String ident) {
        this.flags = (byte) ((ident.startsWith("^") ? Node.ROOT : 0) +
                (ident.startsWith("!") ? Node.IGNORE : 0) +
                (ident.startsWith("@") ? Node.SUBSUME : 0));
        this.identBase = flags != 0 ? ident.substring(1) : ident;
        this.identDot = this.identBase + '_';
    }

//    @Override
//    public Progress explore(Progress progress, boolean ignore) {
////        State state = progress.getState();
////        Progress after = progress.getNext();
//        List<Production> list = Engine.engine().findProductions(identBase);
////        State nextState = after.getState();
//        Progress nextStep = Engine.engine().getProgress(progress.getProduction(), progress.getIndex() + 1);
//        boolean proceed = nextStep == null;
//        if (proceed) nextStep = new Progress(progress.getProduction(), );
//        Engine.engine().index(nextStep, true);
//        state.link(identBase, next);
//        if (proceed) {
//            for (Production prod : list) {
//                Progress progress1 = new Progress(prod, 0, state);
//                if (!progress1.isDup())
//                    progress1.explore();
//            }
//        }
//        return after;
//    }

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
            matched.add(node.newMatch(flags));
            Node next = node.getNextWrapped();
            return next;
        }
        return null;
    }

    @Override
    public Node parse(Node from, List<Node.Match> matched) {
        if (from.isNamed(identBase)) {
            matched.add(from.newMatch(flags));
            Node next = from.getNextWrapped();
            return next;
        }
//        if (from.getParent() == null) return null;
        Node next = Engine.engine().parse(from, identBase);
        if (next != null) {
            Node node = next.getPrevWrapped();
            matched.add(node.newMatch(flags));
        }
        return next;
    }

    @Override
    public String toString() {
        return identBase;
    }

}
