package com.bulbels.game.models.balls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.bulbels.game.models.shapes.AllShapes;
import com.bulbels.game.screens.GameField;

public class SpecialBall extends Ball {

    SpecialBall(float x, float y, float velX, float velY) {
        maxVelocity/=3f;
        launch(velX,velY);
        diameter = AllBalls.radius*2;
        spriteBall = new Sprite(AllShapes.textureAtlas.findRegion("ball"));
        spriteBall.setSize(diameter,diameter);
        spriteBall.setColor(80/250f,0,200/250f,1);
        ballCircle= new Circle(x, y, diameter /2f);
    }

    @Override
    public void move() {
            old_x=getX();
            old_y=getY();
            ballCircle.x+=velocityX* GameField.coefficientFps;
            ballCircle.y+=velocityY*GameField.coefficientFps;
            if (getBottomX() >= Gdx.graphics.getWidth()|| getBottomX()+diameter<=0 ||
                getBottomY() >= GameField.topY || getBottomY()+ diameter<=GameField.bottomY)AllBalls.delete(this);
    }
}
