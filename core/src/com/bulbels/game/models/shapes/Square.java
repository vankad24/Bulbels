package com.bulbels.game.models.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Pool;
import com.bulbels.game.models.balls.AllBalls;
import com.bulbels.game.models.balls.Ball;
import com.bulbels.game.screens.GameField;
import com.bulbels.game.utils.ShapeData;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor;
import static com.bulbels.game.Bulbels.coefficientWidth;
import static com.bulbels.game.models.shapes.AllShapes.*;

public class Square extends Shape {
    Runnable end;

    public Rectangle rectangle;

    Square(String textureName,GameField field) {
        super(textureName, field);
    }


    public Square(GameField field) {
        super( "square", field);


    }

    @Override
    public void createShape() {
        rectangle = new Rectangle();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        /*sprite.setX(rectangle.x);
        sprite.setY(rectangle.y);
        sprite.draw(batch);*/
        sprite.setScale(getScaleX(), getScaleY());
        sprite.setPosition(getX(), getY());
        sprite.setColor(getColor());
        sprite.draw(batch);

        if (!destroying) {
            glyphLayout.setText(font, health+"");
            font.draw(batch, glyphLayout, rectangle.x + (width - glyphLayout.width) / 2, rectangle.y + (width + glyphLayout.height) / 2);
        }
    }

    @Override
    protected void reflection() {

 /*       float len = (float) Math.hypot(ball.getX()-ball.old_x, ball.getY()-ball.old_y);
        float ratio = (float) (Math.sqrt((len+AllBalls.radius)/len));
        ball.setX(ball.old_x+(ball.getX()-ball.old_x)*ratio);
        ball.setY(ball.old_y+(ball.getY()-ball.old_y)*ratio);
        main = new Vector2(ball.getX()-ball.old_x,ball.getY()-ball.old_y);
        if (normalizeCollision(rectangle.x+width,rectangle.y,rectangle.x,rectangle.y)||
                normalizeCollision(rectangle.x+width,rectangle.y+width,rectangle.x+width,rectangle.y)||
                normalizeCollision(rectangle.x,rectangle.y,rectangle.x,rectangle.y+width)||
                normalizeCollision(rectangle.x,rectangle.y+width,rectangle.x+width,rectangle.y+width)){}
*/
        if (rectangle.x > ball.getX() || rectangle.x + width < ball.getRight())
            ball.reflectionWall();
        else if (rectangle.y > ball.getY() || rectangle.y + width < ball.getTop())
            ball.reflectionCeiling();
    }

    @Override
    public void checkCollision(Ball ball) {
        this.ball = ball;
        if (Intersector.overlaps(ball.ballCircle, rectangle)) {
            if (!ball.ghost) reflection();
            updateColor(health -= AllBalls.damage);
            if (health <= 0) {
                destroying = true;
                destroy();
            }
        }

    }


    @Override
    protected void positionChanged() {
        rectangle.setPosition(getX(), getY());
    }

    @Override
    protected void sizeChanged() {
        rectangle.setSize(getWidth(), getHeight());

    }

        @Override
    public void destroy() {

        end = new Runnable() {
            @Override
            public void run() {
                field.game.audioManager.puck();
            }
        };
        field.game.audioManager.puck();
        super.destroy();

//        addAction(sequence(delay(.0f),run(end)));

    }

    @Override
    public ShapeData getData() { return new ShapeData(1,health,maxHealth,getX(),getY()); }

}
