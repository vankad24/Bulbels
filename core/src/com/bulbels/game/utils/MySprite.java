package com.bulbels.game.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MySprite extends Sprite {
    public MySprite() {
    }

    public MySprite(Texture texture) {
        super(texture);
    }

    public MySprite(Texture texture, int srcWidth, int srcHeight) {
        super(texture, srcWidth, srcHeight);
    }

    public MySprite(Texture texture, int srcX, int srcY, int srcWidth, int srcHeight) {
        super(texture, srcX, srcY, srcWidth, srcHeight);
    }

    public MySprite(TextureRegion region) {
        super(region);
    }

    public MySprite(TextureRegion region, int srcX, int srcY, int srcWidth, int srcHeight) {
        super(region, srcX, srcY, srcWidth, srcHeight);
    }

    public MySprite(Sprite sprite) {
        super(sprite);
    }

    public MySprite color(Color tint) {
        super.setColor(tint);
        return this;
    }

    public MySprite size(float width, float height){
        super.setSize(width, height);
        return this;
    }
}
