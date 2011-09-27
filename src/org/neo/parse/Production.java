package org.neo.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.neo.Log;

import org.neo.Node;
import org.neo.Plugin;

/**
 *
 * @author Troy Heninger
 */
public class Production extends RuleGroup {

    public final Plugin plugin;
    public final String name;
    public final String definition;
    public int index = 0;

    public Production(Plugin plugin, String name, String definition) {
        super(null);
        this.plugin = plugin;
        this.name = name;
        this.definition = definition;
        rules = parseRules();
    }

    public String getName() { return name; }
    public Plugin getPlugin() { return plugin; }

    private static Stack<Rule> addAlternatives(Stack<Rule> local, List<List<Rule>> alternatives) {
        if (alternatives != null) {
            alternatives.add(local);
            Rule rule = new RuleOr(alternatives);
            local = new Stack<Rule>();
            local.push(rule);
        }
        return local;
    }

    private static void addIdent(StringBuilder ident, List<Rule> local) {
        if (ident.length() > 0) {
            local.add(new RuleIdent(ident.toString()));
            ident.setLength(0);
        }
    }

    @Override
    public Node match(Node node, List<Node.Match> matched) {
        Node next = super.match(node, matched);
        if (next != null) {
            Node newNode = new Node(plugin, name);
            node.insertBefore(newNode);
            for (Node.Match n : matched) {
                newNode.add(n.node());
            }
            Log.info(name + " matched "
                    + newNode.childNames());
        }
        matched.clear();
        return next;
    }

    private List<Rule> parseRules() {
        Stack<Rule> local = new Stack<Rule>();
        List<List<Rule>> alternatives = null;
        StringBuilder ident = new StringBuilder();
        while (index < definition.length()) {
            char ch = definition.charAt(index++);
            switch (ch) {
                case '*':
                    addIdent(ident, local);
                    local.add(new RuleStar(local.pop()));
                    break;
                case '?':
                    addIdent(ident, local);
                    local.add(new RuleOpt(local.pop()));
                    break;
                case '+':
                    addIdent(ident, local);
                    local.add(new RulePlus(local.pop()));
                    break;
                case '-':
                    addIdent(ident, local);
                    local.add(new RuleNot(local.pop()));
                    break;
                case '|':
                    addIdent(ident, local);
                    if (alternatives == null) alternatives = new ArrayList<List<Rule>>();
                    alternatives.add(local);
                    local = new Stack<Rule>();
                    break;
                case '(':
                    addIdent(ident, local);
                    local.add(new RuleGroup(parseRules()));
                    break;
                case ')':
                    addIdent(ident, local);
                    local = addAlternatives(local, alternatives);
                    return local;
                case '[':
                    addIdent(ident, local);
                    local.add(new RuleInside(parseRules()));
                    break;
                case ']':
                    addIdent(ident, local);
                    local = addAlternatives(local, alternatives);
                    return local;
                case '<':
                    addIdent(ident, local);
                    if (local.size() > 0) {
                        Rule before = new RuleBefore(local);
                        local = new Stack<Rule>();
                        local.add(before);
                    }
                    break;
                case '>':
                    addIdent(ident, local);
                    List<Rule> after = parseRules();
                    if (after.size() > 0) {
                        local.add(new RuleAfter(after));
                    }
                    break;
                case '/':
                    addIdent(ident, local);
                    local.add(new RuleEnforce(parseRules()));
                    break;
//                case ']':
//                    addIdent(ident, local);
//                    local = addAlternatives(local, alternatives);
//                    return local;
                case ' ':
                    addIdent(ident, local);
                    break;
                default: ident.append(ch);
            }
        }
        addIdent(ident, local);
        local = addAlternatives(local, alternatives);
        return local;
    }

    @Override
    public String toString() {
        return name + ": " + definition;
    }

}
