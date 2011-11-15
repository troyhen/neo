package org.neo.parse;

import java.util.List;
import java.util.Set;

class RuleAfter extends RuleGroup {
    
    RuleAfter(List<OptimizedRule> rules) {
        super(rules);
    }

    @Override
    public Node parse(Node from, List<Node.Match> matched) {
        int size = matched.size();
        try {
            super.parse(from, matched);
        } finally {
            Node.revert(matched, size);
        }
        return from;
    }

    @Override
    public String toString() {
        return " > " + super.toString();
    }

}
