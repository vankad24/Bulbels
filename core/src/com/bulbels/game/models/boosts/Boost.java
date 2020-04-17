package com.bulbels.game.models.boosts;

import com.bulbels.game.screens.GameField;

public abstract class Boost {
    static public GameField gameField;
    int time;
    Thread timer= new Thread(new Runnable() {
        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            try { Thread.sleep(time); } catch (InterruptedException e) {}
            end();
        }
    });
    public void setTime(int time) {this.time = time;}
    public abstract void activate();
    public abstract void end();

}
