package com.bulbels.game.models.boosts;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.bulbels.game.screens.GameField;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class GhostBoost extends Boost {

    @Override
    public void activate(int level) {
        GameField.ghost = true;
        GameField.action = sequence(alpha(.5f),delay(.8f+0.4f*level),alpha(1));
    }

    @Override
    public void end() {
        GameField.ghost = false;
    }

    @Override
    public String getRegionName() {
        return "ghost_boost";
    }
}
