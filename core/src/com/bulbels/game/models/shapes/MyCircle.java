package com.bulbels.game.models.shapes;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.bulbels.game.models.balls.AllBalls;
import com.bulbels.game.models.balls.Ball;
import com.bulbels.game.screens.GameField;
import com.bulbels.game.utils.ShapeData;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.after;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.bulbels.game.models.shapes.AllShapes.width;

public class MyCircle extends Shape {
    Circle circle;

    MyCircle(String textureName, GameField field) {
        super(textureName,field);
    }
    public MyCircle(GameField field) {
        super("ball",field);
    }
    @Override
    public void createShape() {circle= new Circle();}

    @Override
    protected void positionChanged() {
        circle.setPosition(getX()+circle.radius,getY()+circle.radius);
    }

    @Override
    protected void sizeChanged() {
        circle.setRadius(getWidth()/2);
        sprite.setSize(getWidth(),getHeight());
        setOrigin(Align.center);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setScale(getScaleX(),getScaleY());
        sprite.setPosition(getX(),getY());
        sprite.setColor(getColor());
        sprite.draw(batch);

        if (!destroying){
            glyphLayout.setText(font,health+"");
            font.draw(batch,glyphLayout,circle.x-glyphLayout.width/2, circle.y+glyphLayout.height/2);
        }


    }



    @Override
    protected void reflection() {
        if (circle.x>ball.getX() || circle.x+circle.radius<ball.getRight() )ball.reflectionWall();
        else if (circle.y>ball.getY() || circle.y+circle.radius<ball.getTop())ball.reflectionCeiling();
    }

    @Override
    public void checkCollision(Ball ball) {
        this.ball = ball;
        if (Intersector.overlaps(ball.ballCircle, circle)) {
            if (!ball.ghost)reflection();
            updateColor(health -= AllBalls.damage);
            if (health <= 0){
                destroying = true;
                destroy();
            }
        }
    }

    @Override
    public void destroy() {
        Runnable end = new Runnable() {
            @Override
            public void run() {
                field.game.audioManager.puck();
            }
        };

//        addAction(after(sequence(delay(.4f),run(end))));
        super.destroy();
        field.game.audioManager.puck();

    }

    @Override
    public ShapeData getData() { return new ShapeData(2,health,maxHealth,getX(),getY()); }

}
