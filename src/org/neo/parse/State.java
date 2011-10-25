package org.neo.parse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a node in a parser state machine.
 * @author Troy Heninger
 * @see Progress
 */
class State {

    private static int nextId = 1;

    private Production production;
    private Map<String, State> links = new HashMap<String, State>();
    private boolean deadEnd;
    private byte flags;
    private int id;
    private State nextState;

    State(Production production) {
        this.production = production;
        this.id = nextId++;
    }

    State(Progress progress) {
        this(progress.getProduction());
        progress.setState(this);
    }

//    void add(Progress progress) {
//        progress.setState(this);
////        progress.explore();
//    }

    Production getProduction() {
        return production;
    }

    byte getFlags() {
        return flags;
    }

    boolean isDeadEnd() {
        return deadEnd;
    }

    void link(String name, State state) {
        if (links.get(name) != null) throw new GrammarException(this.getProduction().getName() + " (" + this.getProduction().plugin.getClass() + ") has more than one " + name + " transition (shift-shift conflict)");
        if (state != null) links.put(name, state);
        else links.remove(name);
    }

    State parse(Node node, List<Node.Match> matched) {
        if (links.isEmpty()) {
//            set.iterator().next().getProduction().reduce(node, matched);
            getProduction().reduce(node, matched);
            return Start.over;
        }
        String name = node.getName();
        State state = links.get(name);
        if (state == null) {
            int ix = name.lastIndexOf('_');
            if (ix > 0) {
                name = name.substring(0, ix + 1);
                state = links.get(name);
            }
        }
        if (state != null) {
            matched.add(node.newMatch(flags));
        }
        return state;
    }

    void setDeadEnd(boolean deadEnd) {
        this.deadEnd = deadEnd;
    }

    void setFlags(byte flags) {
        this.flags = flags;
    }

    void setGoto(State nextState) {
        this.nextState = nextState;
    }

    @Override
    public String toString() {
        return "State " + id;
    }
}
