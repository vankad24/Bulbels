package com.bulbels.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.bulbels.game.models.TextureManager;
import com.bulbels.game.screens.MainMenu;
import com.bulbels.game.screens.MyScreen;
import com.bulbels.game.screens.ScreenManager;
import com.bulbels.game.utils.AndroidHelper;

public class Bulbels extends Game implements ScreenManager {
    public SpriteBatch batch;
    public BitmapFont font;
    public AndroidHelper androidHelper;
    public TextureManager textureManager;
    public TextureAtlas atlas;
    public Bulbels(AndroidHelper androidHelper) {
        this.androidHelper = androidHelper;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        font =new BitmapFont(Gdx.files.internal("myFont.fnt"));
        textureManager = new TextureManager();
        atlas = textureManager.getManager().get("myAtlas.atlas");
        System.out.println(numberOfScreens());
        add(new MainMenu(this));
        System.out.println(numberOfScreens());

    }


    @Override
    public MyScreen add(MyScreen screen) {
        setScreen(screen);
        return SCREENS.push(screen);
    }

    @Override
    public MyScreen remove() {
        SCREENS.pop().dispose();
        if (!SCREENS.empty())setScreen(SCREENS.peek());
        return null;
    }

    @Override
    public void back() {
        SCREENS.peek().back();
    }

    @Override
    public void dispose() {
        getScreen().dispose();
    }

    public int numberOfScreens(){return SCREENS.size();}
}
