package org.neo.parse;

import java.util.List;
import org.neo.parse.Node.Match;

/**
 *
 * @author Troy Heninger
 */
class RuleEnforce extends RuleGroup {

    public RuleEnforce(List<Rule> parseRules) {
        super(parseRules);
    }

    @Override
    public Node match(Node node, List<Match> matched) {
        Node found = super.match(node, matched);
        if (found == null) throw new ParseException("invalid syntax at line " + node.getLine());
        return found;
    }

}
