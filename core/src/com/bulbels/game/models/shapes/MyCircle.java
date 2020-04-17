package com.bulbels.game.models.shapes;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.bulbels.game.models.balls.AllBalls;
import com.bulbels.game.models.balls.Ball;
import com.bulbels.game.screens.GameField;

public class MyCircle extends Shape {
    Circle circle;
    float radius;
    GlyphLayout glyphLayout;
    public MyCircle(float x, float y, int health) {
        font = AllShapes.font;
        this.health = health;
        this.radius=AllShapes.width/2f;
        circle =new Circle(x+radius,y+radius,radius);
        sprite = new Sprite(AllShapes.textureAtlas.findRegion("ball"));

        sprite.setSize(AllShapes.width,AllShapes.width);
        RGB = convertToColor(health);
        sprite.setColor(RGB[0]/255f,RGB[1]/255f,RGB[2]/255f,1);

        steps = 50;
        stepGrowth = radius*.3f/steps;
    }

    @Override
    public void drawShape(SpriteBatch batch) {
        sprite.setX(circle.x-radius);
        sprite.setY(circle.y-radius);
        sprite.draw(batch);

        if (destroying){
            if (alpha-255f/steps* GameField.coefficientFps<=0)destroy();
            else {
                sprite.setAlpha((alpha -= 255f/steps*GameField.coefficientFps)/255f);
                sprite.setSize((radius+=stepGrowth)*2,radius*2);
            }
        }else{
            glyphLayout = new GlyphLayout(font,String.valueOf(health));
            font.draw(batch,glyphLayout,circle.x-glyphLayout.width/2, circle.y+glyphLayout.height/2);
        }
    }

    @Override
    protected void reflection() {
        if (circle.x>ball.getX() || circle.x+radius<ball.getX() )ball.reflectionWall();
        else if (circle.y>ball.getY() || circle.y+radius<ball.getY())ball.reflectionCeiling();
    }

    @Override
    public void checkCollision(Ball ball) {
        this.ball = ball;
        if (Intersector.overlaps(ball.ballCircle, circle)) {
            if (!ball.ghost)reflection();
            RGB = convertToColor(health-= AllBalls.damage);
            sprite.setColor(RGB[0] / 255f, RGB[1] / 255f, RGB[2] / 255f, 1);
            if (health <= 0) destroying = true;
        }
    }
    @Override
    public void setX(float x) { circle.x=x+radius;}

    @Override
    public void setY(float y) { circle.y=y+radius;}

    @Override
    public void addX(float addX) {circle.x+=addX;}

    @Override
    public void addY(float addY) {circle.y+=addY;}



    public void setRadius(float radius) {this.radius = radius;}
}
