package com.bulbels.game.models.boosts;

import com.bulbels.game.models.balls.Ball;

public class GhostBoost extends Boost {

    @Override
    public void activate() {
        for (int i = 0; i <gameField.allBalls.getArrayBalls().size ; i++) {
            gameField.allBalls.getArrayBalls().get(i).ghost=true;
            gameField.allBalls.getArrayBalls().get(i).spriteBall.setAlpha(.5f);
        }
        time = 1500;
        timer.start();
    }

    @Override
    public void end() {
        for (int i = 0; i <gameField.allBalls.getArrayBalls().size ; i++) {
            gameField.allBalls.getArrayBalls().get(i).ghost=false;
            gameField.allBalls.getArrayBalls().get(i).spriteBall.setAlpha(1);
        }
        gameField.boost = null;
    }
}
