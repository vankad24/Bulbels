package com.bulbels.game.models.boosts;

public class ControlledAimBoost extends Boost {

    @Override
    public void activate() {
        gameField.controlledAim= true;
        System.out.println("active");
    }

    @Override
    public void end() {

        gameField.controlledAim= false;
        gameField.boost = null;
    }
}
