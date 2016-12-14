package com.oreilly.demo.android.pa.uidemo.model;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Monsters {

    public List<Monster> monsters;
    public int n, m;

    //creating an arrayList for monsters
    public Monsters (int _n, int _m) {
        monsters = new ArrayList<Monster>();
        n = _n;
        m = _m;
    }
    //adding the monsters of type Monster to the arrayList
    public void addMonster(int i, int j, int x, int y, int r) {
        monsters.add(new Monster(i, j, x, y, r));
    }

    /**
     * migrating between cells: random monster to random neighbour cell
     */
    public void migrate() {
        if (monsters.isEmpty()) return;

        int i = (int) (Math.random() * monsters.size());
        int p1 = (int) (Math.random()*2), p2 = (int) (Math.random()*2);

        Monster monster = monsters.get(i);
        if (p1 == 0 && monster.i - 1 >= 0) monster.i--;
        if (p1 == 1 && monster.i + 1 < m) monster.i++;

        if (p2 == 0 && monster.j - 1 >= 0) monster.j--;
        if (p2 == 1 && monster.j + 1 < n) monster.j++;

        monster.setXY(monster.i, monster.j);

    }

    /**
     * changing monster state: to protected, then after time returning back to vulnerble
     */
    public void changeState() {
        if (monsters.isEmpty()) return;

        int i = (int) (Math.random() * monsters.size());
        Monster monster = monsters.get(i);
        if (monster.state == Monster.State.prot) return;

        monster.state = Monster.State.prot;

        Timer return_timer = new Timer();
        return_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (monster != null) monster.state = Monster.State.vulnerble;
                return_timer.cancel();

            }
        }, 5000);

    }





}
