package com.bulbels.game.models.balls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.bulbels.game.models.shapes.AllShapes;
import com.bulbels.game.screens.GameField;

import static com.bulbels.game.models.balls.AllBalls.maxVelocity;

public class SpecialBall extends Ball {

    SpecialBall(float x, float y, float velX, float velY) {
        super(x,y);
        sprite.setRegion(AllShapes.textureAtlas.findRegion("ball"));
        launch(velX,velY);
        sprite.setColor(80/250f,0,200/250f,1);
    }

    @Override
    public void normalizeVelocity(float x,float y){
        float width = x-getCenterX();
        float height = y-getCenterY();
        float index =(float)(maxVelocity/3f/Math.hypot(width,height));
        velocityX = width*index;
        velocityY = height*index;
    }
    @Override
    public void move() {
            old_x=getCenterX();
            old_y=getCenterY();
            moveBy(velocityX* GameField.coefficientFps,velocityY*GameField.coefficientFps);
            if (getBottomX() >= Gdx.graphics.getWidth()|| getBottomX()+diameter<=0 ||
                getBottomY() >= GameField.topY || getBottomY()+ diameter<=GameField.bottomY)AllBalls.delete(this);
    }
}
