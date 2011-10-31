package org.neo.parse;

import org.neo.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a node in a parser state machine.
 * @author Troy Heninger
 * @see Progress
 */
class State {

//    private Production production;
    private Progress progress;
    private Map<String, State> links = new HashMap<String, State>();
    private boolean deadEnd;
    private byte flags;
    private int id;
//    private State nextState;

//    State(Production production) {
//        this.production = production;
//        this.id = Engine.engine().add(this);
//    }

    State(Progress progress) {
//        this(progress.getProduction());
        this.id = Engine.engine().add(this);
        this.progress = progress;
        progress.setState(this);
    }

//    void add(Progress progress) {
//        progress.setState(this);
////        progress.explore();
//    }

//    Production getProduction() {
//        return progress.getProduction();
//    }

    byte getFlags() {
        return flags;
    }

    int getId() {
        return id;
    }

    Progress getProgress() {
        return progress;
    }

    boolean isDeadEnd() {
        return deadEnd;
    }

    void link(String name, State state) {
        final State state1 = links.get(name);
        if (state1 != null) {
            if (name.equals("*")) Log.warning(this.getProgress() + " (" + this.getProgress().getProduction().plugin.getClass() + ") has more than one " + name + " transition (shift-shift conflict)");
            else throw new GrammarException(this.getProgress() + " (" + this.getProgress().getProduction().plugin.getClass() + ") has more than one " + name + " transition (shift-shift conflict)");
        }
        if (state != null) links.put(name, state);
        else links.remove(name);
    }

    void merge(State other) {
        Engine.engine().merge(this, other);
    }

    State parse(Node node, List<Node.Match> matched) {
        if (links.isEmpty()) {
//            set.iterator().next().getProduction().reduce(node, matched);
            getProgress().getProduction().reduce(node, matched);
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

    public void replace(State other, State state) {
        for (Map.Entry<String, State> entry : links.entrySet()) {
            if (entry.getValue() == other)
                entry.setValue(state);
        }
    }

    void setDeadEnd(boolean deadEnd) {
        this.deadEnd = deadEnd;
    }

    void setFlags(byte flags) {
        this.flags = flags;
    }

//    void setGoto(State nextState) {
//        this.nextState = nextState;
//    }

    @Override
    public String toString() {
        return "Step " + progress.getIndex() + " of " + progress.getProduction() + " (State " + id + ')';
    }
}
