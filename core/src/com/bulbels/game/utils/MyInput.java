package com.bulbels.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.bulbels.game.Bulbels;
import com.bulbels.game.models.balls.AllBalls;
import com.bulbels.game.models.boosts.ControlledAimBoost;
import com.bulbels.game.models.boosts.DoubleDamageBoost;
import com.bulbels.game.models.boosts.GhostBoost;
import com.bulbels.game.models.boosts.SizeBoost;
import com.bulbels.game.models.boosts.SpeedBoost;
import com.bulbels.game.screens.GameField;

public class MyInput implements InputProcessor {
    public GameField field;


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        field.can = true;
        switch (pointer) {
            case 0:
                field.touchDown(screenX, screenY, true);
                break;
            case 1:
                field.allBalls.addBall();
                field.amountOfBalls++;
                break;
            case 2:
                field.turn++;
                break;
            case 3:
                field.allShapes.generateNewLine();
                break;
            case 4:
                field.allBalls.moveWithAnimationTo(Gdx.input.getX(pointer), Gdx.graphics.getHeight() - Gdx.input.getY(pointer));
                break;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        field.touchUp(screenX,screenY);

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        field.can=true;
        field.touchDown(screenX,screenY,false);
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {

//        System.out.println(keycode);
        switch (keycode){
            case 8:
                field.boost= new SpeedBoost();
                break;
            case 9:
                field.boost= new ControlledAimBoost();
                break;
            case 10:
                field.boost= new DoubleDamageBoost();
                break;
            case 11:
                field.boost= new SizeBoost();
                break;
            case 12:
                field.boost= new GhostBoost();
                break;
            case 13:

                break;
            case 14:

                break;
            case 45:
                field.allBalls.addBall();
                field.amountOfBalls++;
                break;
            case 51:
                field.turn++;
                break;
            case 33:
                field.allShapes.generateNewLine();
                break;
            case 46:
                field.allBalls.moveWithAnimationTo(Gdx.input.getX(),Gdx.graphics.getHeight()-Gdx.input.getY());
                break;
            case 32:
                AllBalls.startX = field.allBalls.getArrayBalls().get(0).getX()+30;
                field.allBalls.setX(AllBalls.startX);
                break;
            case 29:
                AllBalls.startX =field.allBalls.getArrayBalls().get(0).getX()-30;
                field.allBalls.setX(AllBalls.startX);
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
