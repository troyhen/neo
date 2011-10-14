package org.neo.parse;

import java.util.List;

/**
 *
 * @author Troy Heninger
 */
class RuleInside extends RuleGroup {

    public RuleInside(List<Rule> parseRules) {
        super(parseRules);
    }

    @Override
    public Node match(Node start, List<Node.Match> matched) {
        int size = matched.size();
        if (size == 0) return null;
        Node node = matched.get(size - 1).getFirst();
        if (node == null) return null;
        node = super.match(node, matched);
        Node.revert(matched, size);
        if (node == null) return null;
        return start;
    }

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder("[");
        String space = "";
        for (Rule rule : rules) {
            buff.append(space);
            space = " ";
            buff.append(rule);
        }
        buff.append("]");
        return buff.toString();
    }

}
