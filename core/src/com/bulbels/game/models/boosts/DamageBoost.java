package com.bulbels.game.models.boosts;

import com.bulbels.game.models.balls.AllBalls;

public class DamageBoost extends Boost {
    @Override
    public void activate(int level) {
        AllBalls.damage+=(1+level);
    }

    @Override
    public void end() {
        AllBalls.damage=1;
    }

    @Override
    public String getRegionName() {
        return "damage_boost";
    }
}
