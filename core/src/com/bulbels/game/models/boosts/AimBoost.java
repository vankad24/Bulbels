package com.bulbels.game.models.boosts;

import com.bulbels.game.screens.GameField;

public class AimBoost extends Boost {
    @Override
    public void activate(int level) {
        time = 1000*(4+2*level);
        if (level==3)time=-1;
        new Timer().start();
        GameField.controlledAim= true;
    }

    @Override
    public void end() {
        GameField.controlledAim= false;
    }

    @Override
    public String getRegionName() {
        return "aim_boost";
    }



}
