package com.bulbels.game.models.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.bulbels.game.models.balls.AllBalls;
import com.bulbels.game.models.balls.Ball;
import com.bulbels.game.screens.GameField;

public class Box extends Shape {
    Rectangle rectangle;
    float width;
    GlyphLayout glyphLayout;

    public Box(float x, float y, int health) {
        font = AllShapes.font;
        this.health = health;
        this.width = AllShapes.width;
        sprite = new Sprite(AllShapes.textureAtlas.findRegion("square"));
        rectangle = new Rectangle(x,y,width,width);
        sprite.setSize(width,width);

        sprite.setColor(1f,150/255f,0,1);
        steps = 36;
        stepGrowth = width*.3f/steps;
    }

    @Override
    public void drawShape(SpriteBatch batch) {
        sprite.setX(rectangle.x);
        sprite.setY(rectangle.y);
        sprite.draw(batch);
        if (destroying){
            if (alpha-255f/steps* GameField.coefficientFps<=0)destroy();
            else {
                sprite.setAlpha((alpha -= 255f/steps*GameField.coefficientFps)/255f);
                sprite.setSize(width+=stepGrowth,width);
                addX(-stepGrowth/2);
                addY(-stepGrowth/2);
            }
        }else{
            glyphLayout = new GlyphLayout(font,String.valueOf(health));
            // System.out.println(glyphLayout.width+" "+glyphLayout.height);
            font.draw(batch,glyphLayout,rectangle.x+(width-glyphLayout.width)/2, rectangle.y+(width+glyphLayout.height)/2);
        }
    }

    @Override
    protected void reflection() {
        if (rectangle.x>ball.getX() || rectangle.x+width<ball.getX() )ball.reflectionWall();
        else if (rectangle.y>ball.getY() || rectangle.y+width<ball.getY())ball.reflectionCeiling();
    }

    @Override
    public void checkCollision(Ball ball) {
        this.ball = ball;
        if (Intersector.overlaps(ball.ballCircle, rectangle)) {
            if (!ball.ghost)reflection();
            health-= AllBalls.damage;
            if (health <= 0) {
                destroying = true;
                drop();
            }
        }

    }

    void drop(){
        int[] chances={60,0,0,0};
        switch (AllShapes.dropWithChance(chances)) {
            case 0:
                AllBalls.addSpecialBalls(5,rectangle.x+width/2f,rectangle.y+width/2f);
                break;
            case 1:

                break;
        }
    }

    @Override
    public void setX(float x) { rectangle.x=x;}

    @Override
    public void setY(float y) { rectangle.y=y;}

    @Override
    public void addY(float addY) {rectangle.y+=addY;}

    @Override
    public void addX(float addX) {rectangle.x+=addX;}
}
