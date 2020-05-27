package com.bulbels.game.models.boosts;

import com.bulbels.game.screens.GameField;

public abstract class Boost {
    public int time;

    public void setTime(int time) {this.time = time;}
    public abstract void activate(int level);
    public abstract void end();
    public abstract String getRegionName();
    public Boost getBoost(){return this;}
    class Timer extends Thread{
        @Override
        public void run() {
            if (time==-1)return;
            long startTime = System.currentTimeMillis();
            try { Thread.sleep(time); } catch (InterruptedException e) {}
            end();
        }
    }
}
