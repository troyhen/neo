package org.dia.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.dia.Log;
import org.dia.Named;
import org.dia.Node;
import org.dia.Plugin;

/**
 *
 * @author Troy Heninger
 */
public class Production extends RuleGroup implements Named {

    public final Plugin plugin;
    public final String name;
    public final String definition;
    public final int complexity;
    public int index = 0;

    public Production(Plugin plugin, String name, String definition) {
        super(null);
        this.plugin = plugin;
        this.name = name;
        this.definition = definition;
        rules = parseRules();
        this.complexity = complexity(); // must be called after parseRules
    }

    @Override
    public String getName() { return name; }

    @Override
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
    public Node match(Node node, List<Node> matched) {
        Node next = super.match(node, matched);
        if (next != null) {
            Node newNode = new Node(this, getName());
            node.insertBefore(newNode);
            for (Node n : matched) {
                newNode.add(n);
            }
        }
        matched.clear();
        return next;
    }

    public Node matched(Node node) {
        try {
            return plugin.matched(name, node);
        } catch (Exception ex) {
            Log.logger.severe(ex.toString());
            throw new ParseException(ex);
        }
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
                    local.add(new RulePeek(parseRules()));
                    break;
                case ']':
                    addIdent(ident, local);
                    local = addAlternatives(local, alternatives);
                    return local;
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

}
