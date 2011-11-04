package org.neo.parse;

import java.util.List;

class RuleGroup implements OptimizedRule {
    
    protected List<OptimizedRule> rules;

    public RuleGroup(List<OptimizedRule> rules) {
        this.rules = rules;
    }

//    @Override
//    public Progress explore(Progress progress, boolean ignore) {
////        Ignore next = null;
////        State state = progress.getState();
//        for (OptimizedRule rule : rules) {
//            progress = rule.explore(progress, ignore);
////            if (next != null) {
////                next = new Ignore();
////                previous.add(null, next);
////            }
////            previous = rule.explore(production, index, previous, ignore);
//        }
//        return progress;
//    }

    @Override
    public boolean findStarts(List<String> list) {
        boolean more = false;
        for (OptimizedRule rule : rules) {
            more = rule.findStarts(list);
            if (!more) break;
        }
        return more;
    }

    @Override
    public Node match(Node node, List<Node.Match> matched) {
        int size = matched.size();
        for (Rule rule : rules) {
            node = rule.match(node, matched);
            if (node == null) {
                Node.revert(matched, size);
                break;
            }
        }
        return node;
    }

    @Override
    public Node parse(Node from, List<Node.Match> matched) {
        int size = matched.size();
        Node node = from;
        try {
            for (OptimizedRule rule : rules) {
                node = rule.parse(node, matched);
            }
        } catch (Mismatch e) {
            Node.revert(matched, size);
            throw e;
        }
        return node;
    }

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder("(");
        String space = "";
        for (Rule rule : rules) {
            buff.append(space);
            space = " ";
            buff.append(rule);
        }
        buff.append(")");
        return buff.toString();
    }

}
