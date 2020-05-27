package com.bulbels.game.models.boosts;

import com.bulbels.game.models.balls.AllBalls;
import com.bulbels.game.models.balls.Ball;

public class SpeedBoost extends Boost{
    float oldVelocity;

    @Override
    public void activate(int level) {
        oldVelocity = AllBalls.maxVelocity;
        float val = 1.25f+.25f*level;
        AllBalls.maxVelocity*=val;
    }

    @Override
    public void end() {
        AllBalls.maxVelocity=oldVelocity;
    }

    @Override
    public String getRegionName() {
        return "speed_boost";
    }
}
