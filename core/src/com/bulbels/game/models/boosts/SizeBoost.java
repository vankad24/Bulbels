package com.bulbels.game.models.boosts;

import com.bulbels.game.models.balls.AllBalls;

public class SizeBoost extends Boost {
    float oldSize;

    @Override
    public void activate() {

        oldSize = AllBalls.size;
        gameField.allBalls.resize(AllBalls.size*1.5f);
        long start = System.currentTimeMillis();
        time = 1000;
        timer.start();


    }

    @Override
    public void end() {
        gameField.allBalls.resize(oldSize);
        gameField.boost = null;
    }
}
