package com.bulbels.game.models.shapes;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.bulbels.game.models.balls.AllBalls;
import com.bulbels.game.models.balls.Ball;
import com.bulbels.game.screens.GameField;
import com.bulbels.game.utils.ShapeData;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.after;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveToAligned;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sizeTo;
import static com.bulbels.game.models.shapes.AllShapes.*;

public class Coin extends MyCircle {
    public Coin(GameField field) {
        super("coin", field);
    }

    @Override
    public void init(float x, float y, int health) {
        super.init(x, y, health);
        setSize(width*0.8f, width*0.8f);
        setOrigin(Align.center);
        setPosition(x+ width/2f,y+ width/2f, Align.center);
        setZIndex(20);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setScale(getScaleX(),getScaleY());
        sprite.setPosition(getX(),getY());
        sprite.draw(batch);
    }

    @Override
    protected void reflection() {}

    @Override
    public void checkCollision(Ball ball) {
        this.ball = ball;
        if (Intersector.overlaps(ball.ballCircle, circle)) {
            destroying = true;
            destroy();
        }
    }

    @Override
    public void destroy() {destroy(true);}
    public void destroy(boolean effect) {
        Runnable remove = new Runnable() {
            @Override
            public void run() {
                field.game.money++;
                field.updateLabels();
                field.game.audioManager.coin();
                delete();
            }
        };
        if (effect)field.game.audioManager.take_coin.play(field.game.audioManager.getVolumeEffects());
        addAction(after(sequence(parallel(moveTo(field.coins.getX(),field.coins.getY(),1f),sizeTo(field.coins.getWidth(),field.coins.getWidth(),1f)),run(remove),removeActor())));
    }

    @Override
    public ShapeData getData() { return new ShapeData(4,health,maxHealth,getX()+getOriginX()-width/2f,getY()+getOriginY()-width/2f); }
}

