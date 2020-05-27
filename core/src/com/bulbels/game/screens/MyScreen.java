package com.bulbels.game.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.bulbels.game.Bulbels;

public abstract class MyScreen implements Screen, InputProcessor {
    public Bulbels game;
    Stage stage;
    Table table;

    Runnable back = new Runnable() {
        @Override
        public void run() {
            if (game.remove().getClass()==MainMenu.class)game.SCREENS.peek().slideBack();
            System.out.println("slideBack");
        }
    };

    MyScreen(Bulbels game){
        stage = new Stage();
        table = new Table();
        this.game = game;
    }


    abstract void slideStart();
    abstract void slideBack();
    abstract public void back();

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE:
                back();
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
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
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
