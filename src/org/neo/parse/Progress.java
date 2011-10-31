package org.neo.parse;

/**
 * Represents a point or step within a production. There are a finite number of steps the parser goes through at it is
 * matching a production in the grammar. Progress objects are associated with nodes in a state machine. Each node can
 * contain multiple Progress points.
 * @author Troy Heninger
 */
class Progress {

    private State state;
    private final Production production;
    private final int index;

//    Progress(Production production, int index) {
//        this(production, index, null);
//    }

    Progress(Production production, int index, State state) {
        this.production = production;
        this.index = index;
        setState(state);
    }

//    static Progress create(Production production, int index) {
//        return create(production, index, null);
//    }
//
//    static Progress create(Production production, int index, State state) {
//        Progress progress = new Progress(production, index, state);
//        Progress progress1 = Engine.engine().getProgress(progress);
//        if (progress1 != null) return progress1;
//        Engine.engine().index(progress);
//        return progress;
//    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Progress)) return false;
        if (this.production != production) return false;
        if (this.index != index) return false;
        return true;
    }

    Progress explore() {
        return production.explore(this, false);
    }

    State getState() {
        if (state == null)
            new State(this);
        return state;
    }

    int getIndex() {
        return index;
    }

    Progress getNext() {
        return Engine.engine().getProgress(production, index + 1);
    }

    Production getProduction() {
        return production;
    }

    @Override
    public int hashCode() {
        return production.hashCode() + index;
    }

    public boolean hasState() {
        return state != null;
    }

//    public boolean isDup() {
//        return dup;
//    }

    public void replace(State other, State state) {
        if (this.state == other) this.state = state;
    }

    void setState(State state) {
        this.state = state;
        if (state != null) Engine.engine().index(this);
    }

    @Override
    public String toString() {
        return production.toString() + ":: step " + index + (state != null ? " from State " + state.getId() : "");
    }

}
