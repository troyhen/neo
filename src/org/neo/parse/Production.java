package org.neo.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.neo.parse.Node.Match;
import org.neo.util.Log;

import org.neo.Plugin;

/**
 * A sequence of rules to match an incoming sequence of tokens. Productions are part of a language grammar and belong
 * to Plugins.
 * @author Troy Heninger
 * @see Engine
 * @see Plugin
 * @see Rule
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

    private static Stack<OptimizedRule> addAlternatives(Stack<OptimizedRule> local, List<List<OptimizedRule>> alternatives) {
        if (alternatives != null) {
            alternatives.add(local);
            OptimizedRule rule = new RuleOr(alternatives);
            local = new Stack<OptimizedRule>();
            local.push(rule);
        }
        return local;
    }

    private static void addIdent(StringBuilder ident, List<OptimizedRule> local) {
        if (ident.length() > 0) {
            local.add(new RuleIdent(ident.toString()));
            ident.setLength(0);
        }
    }

    public List<String> findStarts() {
        List<String> list = new ArrayList<String>();
        findStarts(list);
        return list;
    }

    public boolean isNamed(String name) {
        return this.name.equals(name) || this.name.startsWith(name + '_');
    }

    @Override
    public Node match(Node node, List<Match> matched) {
        matched.clear();
        return super.match(node, matched);
    }

    @Override
    public Node parse(Node from, List<Match> matched) {
        matched.clear();
        return super.parse(from, matched);
    }

    private List<OptimizedRule> parseRules() {
        Stack<OptimizedRule> local = new Stack<OptimizedRule>();
        List<List<OptimizedRule>> alternatives = null;
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
                case '&':
                    addIdent(ident, local);
                    local.add(new RuleTest(local.pop()));
                    break;
                case '|':
                case '/':
                    addIdent(ident, local);
                    if (alternatives == null) alternatives = new ArrayList<List<OptimizedRule>>();
                    alternatives.add(local);
                    local = new Stack<OptimizedRule>();
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
                    local = addAlternatives(local, alternatives);
                    alternatives = null;
                    if (local.size() > 0) {
                        OptimizedRule before = new RuleBefore(local);
                        local = new Stack<OptimizedRule>();
                        local.add(before);
                    }
                    break;
                case '>':
                    addIdent(ident, local);
                    local = addAlternatives(local, alternatives);
                    alternatives = null;
                    List<OptimizedRule> after = parseRules();
                    if (after.size() > 0) {
                        local.add(new RuleAfter(after));
                    }
                    break;
//                case '/':
//                    addIdent(ident, local);
//                    local.add(new RuleEnforce(parseRules()));
//                    break;
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

    public Node reduce(Node node, List<Node.Match> matched) {
        Node newNode = new Node(plugin, name);
            // Move to the first node matched to support RuleBefore
        if (matched.size() > 0) node = matched.get(0).node();
        node.insertBefore(newNode);
        StringBuilder buff = new StringBuilder();
        buff.append("line ");
        buff.append(node.getLine());
        buff.append(" ");
        buff.append(name);
        buff.append(" matched ");
        if (newNode.getPrev() != null) {
//            if (newNode.getPrev().getPrev() != null) {
//                buff.append(newNode.getPrev().getPrev());
//                buff.append(" ");
//            }
            buff.append(newNode.getPrev());
            buff.append(" ");
        }
        buff.append("[");
        String space = "";
        for (Node.Match n : matched) {
            newNode.add(node = n.node());
            buff.append(space);
            space = " ";
            buff.append(n);
        }
        buff.append("] ");
        if (newNode.getNext() != null) {
            buff.append(newNode.getNext());
//            if (newNode.getNext().getNext() != null) {
//                buff.append(" ");
//                buff.append(newNode.getNext().getNext());
//            }
        }
        Log.info(buff.toString());
//        matched.clear();
        return newNode;
    }

    @Override
    public String toString() {
        return name + ": " + definition;
    }
}
