package org.neo.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    public boolean findStarts(Set<String> list) {
//        Set<String> list2 = Engine.engine().getStarts(identBase);
//        if (list2 != null) {
//            list.addAll(list2);
//            return false;
//        }
        list.add(identBase);
//        list.add(identDot);
//        List<Production> children = Engine.engine().findProductions(identBase);
//        for (Production production : children) {
//            production.findStarts(list);
//        }
        return false;
    }

    /**
     * Match the identifier and return the following node or null if cannot be matched. First a check is made that
     * the current node is already what the identifier specified. If not it check the previous memoised results for
     * the node's position. If not yet memoised it parses the rules specified by the identifier. The result, found or
     * not, is memoised.
     * @param from the node to start on
     * @param matched list of matched nodes (used for reduction)
     * @return the following node if a matched or null if not matched
     */
    @Override
    public Node parse(Node from, List<Node.Match> matched) {
        if (from.isNamed(identBase)) {
            matched.add(from.newMatch(flags));
            Node next = from.getNextOrLast();
            return next;
        }
        if (RuleBefore.simpleCheck.get())
            throw new Mismatch(from, identBase);
        Node root = from.getParent();
        try {
            Node newNode = Engine.engine().parse(from, identBase);
            matched.add(newNode.newMatch(flags));
            return newNode.getNextOrLast();
        } catch (Mismatch e) {
            // It's possible to get a mismatch yet still get the desired reduction as a side effect of a partial
            // sub-rule match. This code deals with this situation.
            // TODO it might be better to take care of this lower down and not throw the exception
            while (from.getParent() != root) {
                from = from.getParent();
            }
            if (from.isNamed(identBase)) {
                matched.add(from.newMatch(flags));
                Node next = from.getNextOrLast();
                return next;
            }
            throw e;
        }
    }

    @Override
    public String toString() {
        return identBase;
    }

}
