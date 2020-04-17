package com.bulbels.game.models.boosts;

import com.bulbels.game.models.balls.AllBalls;
import com.bulbels.game.models.balls.Ball;

public class SpeedBoost extends Boost{

    @Override
    public void activate() {
        gameField.allBalls.setMaxVelocity(AllBalls.maxVelocity*2);
    }

    @Override
    public void end() {
        gameField.allBalls.setMaxVelocity(AllBalls.maxVelocity);
        gameField.boost = null;

    }
}
