package com.oreilly.demo.android.pa.uidemo.model;

public class Monster {
    public int i, j; // position in grid
    public int x, y;
    public int r; // radius

    public enum State {
        vulnerable, prot
    }
    public State state;

    public Monster (int _i, int _j, int _r) {
        i = _i;
        j = _j;
        r = _r;
        x = 3*r/2 + 5*r/2*i;
        y = 3*r/2 + 5*r/2*j;

        state = State.vulnerable;
    }

    public void setXY(int i, int j) {
        x = 3*r/2 + 5*r/2*i;
        y = 3*r/2 + 5*r/2*j;
    }

    public boolean canBeEliminated() {
        return state == State.vulnerable;
    }
}
