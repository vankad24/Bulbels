package com.bulbels.game.models.boosts;

import com.bulbels.game.models.balls.AllBalls;

public class DoubleDamageBoost extends Boost {
    @Override
    public void activate() {
        AllBalls.damage*=2;
    }

    @Override
    public void end() {
        AllBalls.damage/=2;
        gameField.boost = null;
    }
}
