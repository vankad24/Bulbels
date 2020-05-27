package com.bulbels.game.models;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class TextureManager {
    public AssetManager manager;


    public TextureManager() {
        manager = new AssetManager();
        manager.load("myAtlas.atlas",TextureAtlas.class);
        manager.load("nameAtlas.atlas",TextureAtlas.class);
        manager.finishLoading();
    }

    public AssetManager getManager() { return manager; }

    public void dispose () {
        manager.dispose();
    }
}
