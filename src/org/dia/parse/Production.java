package org.dia.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.dia.Compiler;
import org.dia.Log;
import org.dia.Node;
import org.dia.Plugin;

/**
 *
 * @author Troy Heninger
 */
public class Production extends RuleGroup {

    public final Plugin plugin;
    public final String name;

    public Production(Plugin plugin, String name, String definition) {
        super(parseRules(definition));
        this.plugin = plugin;
        this.name = name;
    }

    private static void addIdent(StringBuilder ident, List<Rule> local) {
        if (ident.length() > 0) {
//            if (ident.charAt(0) == '^') {
//                ident.deleteCharAt(0);
//                local.add(new RuleRoot(new RuleIdent(ident.substring(1))));
//            } else {
                local.add(new RuleIdent(ident.toString()));
//            }
            ident.setLength(0);
        }
    }
    
//    @Override
//    public Node match(boolean first) {
//        Node node = super.match(true);
//        if (node != null) node = matched(node);
//        return node;
//    }

//    @Override
    public Node matched(Node node) {
        try {
            return plugin.matched(name, node);
        } catch (Exception ex) {
            Log.logger.severe(ex.toString());
            throw new ParseException(ex);
        }
    }

    private static List<Rule> parseRules(CharSequence definition) {
        List<Rule> local = new ArrayList<Rule>();
        StringBuilder ident = new StringBuilder();
        for (int index = 0, end = definition.length(); index < end; index++) {
            char ch = definition.charAt(index);
            switch (ch) {
                case '*':
                    addIdent(ident, local);
                    local.add(new RuleStar(local.remove(local.size() - 1)));
                    break;
                case '?':
                    addIdent(ident, local);
                    local.add(new RuleOpt(local.remove(local.size() - 1)));
                    break;
                case '+':
                    addIdent(ident, local);
                    local.add(new RulePlus(local.remove(local.size() - 1)));
                    break;
                case '-':
                    addIdent(ident, local);
                    local.add(new RuleNot(local.remove(local.size() - 1)));
                    break;
                case '(':
                    addIdent(ident, local);
                    local.add(new RuleGroup(parseRules(definition.subSequence(index + 1, definition.length()))));
                    break;
                case ')':
                    addIdent(ident, local);
                    return local;
                case ' ':
                    addIdent(ident, local);
                    break;
                default: ident.append(ch);
            }
        }
        addIdent(ident, local);
        return local;
    }

}
