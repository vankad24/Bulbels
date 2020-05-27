package com.bulbels.game.models.boosts;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.bulbels.game.models.balls.AllBalls;
import com.bulbels.game.screens.GameField;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sizeBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sizeTo;

public class SizeBoost extends Boost {
    float oldSize;

    @Override
    public void activate(int level) {
        oldSize = AllBalls.radius*2;
        float newSize = oldSize*(.1f+.1f*level);
        float duration = .8f+.2f*level;
        GameField.action = sequence(sizeBy(newSize,newSize,duration),delay(duration),sizeTo(oldSize,oldSize,duration/3));
    }

    @Override
    public void end() {
    }

    @Override
    public String getRegionName() {
        return "size_boost";
    }
}
