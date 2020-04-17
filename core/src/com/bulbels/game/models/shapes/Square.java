package com.bulbels.game.models.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bulbels.game.models.balls.AllBalls;
import com.bulbels.game.models.balls.Ball;
import com.bulbels.game.screens.GameField;

public class Square extends Shape {


    Rectangle rectangle;
    float width;
    GlyphLayout glyphLayout;

    public Square(float x, float y, int health) {
        font = AllShapes.font;
        this.health = health;
        this.width = AllShapes.width;
        sprite = new Sprite(AllShapes.textureAtlas.findRegion("square"));
        rectangle = new Rectangle(x,y,width,width);
        sprite.setSize(width,width);
        RGB = convertToColor(health);
        sprite.setColor(RGB[0]/255f,RGB[1]/255f,RGB[2]/255f,1);
        steps = 36;
        stepGrowth = width*.3f/steps;

        test = new Sprite(AllShapes.textureAtlas.findRegion("square"));
        test.setSize(2,2);
        test.setColor(Color.RED);
    }

    @Override
    public void drawShape(SpriteBatch batch) {
        sprite.setX(rectangle.x);
        sprite.setY(rectangle.y);
        sprite.draw(batch);
        test.draw(batch);
        if (destroying){
            if (alpha-255f/steps*GameField.coefficientFps<=0)destroy();
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

        /*float len = (float) Math.hypot(ball.getX()-ball.old_x, ball.getY()-ball.old_y);
        float ratio = (float) (Math.sqrt((len+AllBalls.radius)/len));
        ball.setX(ball.old_x+(ball.getX()-ball.old_x)*ratio);
        ball.setY(ball.old_y+(ball.getY()-ball.old_y)*ratio);
        main = new Vector2(ball.getX()-ball.old_x,ball.getY()-ball.old_y);
        if (normalizeCollision(rectangle.x+width,rectangle.y,rectangle.x,rectangle.y)||
                normalizeCollision(rectangle.x+width,rectangle.y+width,rectangle.x+width,rectangle.y)||
                normalizeCollision(rectangle.x,rectangle.y,rectangle.x,rectangle.y+width)||
                normalizeCollision(rectangle.x,rectangle.y+width,rectangle.x+width,rectangle.y+width)){}
*/
        if (rectangle.x>ball.getX() || rectangle.x+width<ball.getX() )ball.reflectionWall();
        else if (rectangle.y>ball.getY() || rectangle.y+width<ball.getY())ball.reflectionCeiling();
    }

    @Override
    public void checkCollision(Ball ball) {
        this.ball = ball;
        if (Intersector.overlaps(ball.ballCircle, rectangle)) {
            if (!ball.ghost)reflection();
            RGB = convertToColor(health-= AllBalls.damage);
            sprite.setColor(RGB[0] / 255f, RGB[1] / 255f, RGB[2] / 255f, 1);
            if (health <= 0) destroying = true;
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
