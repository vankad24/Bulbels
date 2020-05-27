package com.bulbels.game.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

public class MyActor extends Actor {
    public Sprite sprite;
    @Override
    protected void positionChanged() {
        sprite.setPosition(getX(),getY());
    }

    @Override
    protected void sizeChanged() {
        sprite.setSize(getWidth(),getHeight());
        setOrigin(Align.center);

    }

    public Sprite getSprite() {
        return sprite;
    }

    public MyActor(TextureRegion region) {
        this.sprite = new Sprite(region);
    }
    public MyActor(TextureRegion region, float x, float y, float width, float height) {
        if (region==null)this.sprite = new Sprite();
        else this.sprite = new Sprite(region);

        setSize(width,height);
        setPosition(x,y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
//        System.out.println(" "+getX());
        sprite.setPosition(getX(),getY());
        sprite.setSize(getWidth(),getHeight());
        sprite.setColor(getColor());
        sprite.draw(batch);
//        SpriteDrawable drawable = new SpriteDrawable(sprite);
//        drawable.draw(batch, getX(),getY(),getWidth(),getHeight());
    }
}
