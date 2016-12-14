package com.oreilly.demo.android.pa.uidemo.model;

public class Monster {
    public int i, j; // position in grid
    public int x, y;
    public int r; // radius

    public enum State {
        vulnerable, prot
    }
    public State state;

    public Monster (int _i, int _j, int _x, int _y, int _r) {
        i = _i;
        j = _j;
        x = _x;
        y = _y;
        r = _r;

        state = State.vulnerable;
    }

    public void setXY(int i, int j) {
        x = 150 + 250*j;
        y = 150 + 250*i;
    }

    public boolean canBeEliminated() {
        return state == State.vulnerable;
    }
}
