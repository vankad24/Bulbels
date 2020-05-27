package com.bulbels.game.models.balls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.bulbels.game.Bulbels;
import com.bulbels.game.models.shapes.AllShapes;
import com.bulbels.game.screens.GameField;

import static com.bulbels.game.models.balls.AllBalls.maxVelocity;

public class Ball extends Actor {

    float velocityX,velocityY;
    public float old_x, old_y;
    boolean motion, resizing;
    public boolean ghost;

    public Sprite sprite;
    public Circle ballCircle;
    float diameter;
    public Ball(float x, float y) {
        diameter = AllBalls.radius*2;
        sprite = Bulbels.skin.getSprite();
        sprite.setSize(diameter,diameter);
//        System.out.println("color"+ sprite.getColor());
//        spriteBall.setColor((float) Math.random(),(float) Math.random(),(float) Math.random(),1);
        ballCircle= new Circle(x, y, diameter /2f);
        setCenterX(x);
        setCenterY(y);
        setSize(diameter,diameter);
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        sprite.setSize(getWidth(),getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setScale(getScaleX(),getScaleY());
        sprite.setPosition(getX(),getY());
        sprite.setSize(getWidth(),getHeight());
        sprite.getColor().a = getColor().a;
        sprite.draw(batch);
    }

    @Override
    public void positionChanged() {
        ballCircle.setPosition(getX()+ballCircle.radius,getY()+ballCircle.radius);

    }

    @Override
    protected void sizeChanged() {
        ballCircle.setRadius(getWidth()/2);
    }

    @Override
    protected void rotationChanged() {

    }

    public float getBottomX(){return ballCircle.x-ballCircle.radius;}//Только для рисования
    public float getBottomY(){return ballCircle.y-ballCircle.radius;}//Только для рисования
    /*public float getX(){return ballCircle.x;}
    public float getY(){return ballCircle.y;}*/
    public float getCenterX(){return ballCircle.x;}
    public float getCenterY(){return ballCircle.y;}
    public void setCenterX(float x){setX(x-ballCircle.radius);}
    public void setCenterY(float y){ setY(y-ballCircle.radius);}

    public void reflectionWall(){ velocityX*=-1;}
    public void reflectionCeiling(){velocityY*=-1;}

    public boolean inMotion(){
        return motion;
    }

    public void move(){
        old_x=getCenterX();
        old_y=getCenterY();
        moveBy(velocityX*GameField.coefficientFps,velocityY*GameField.coefficientFps);
        if (getRight()>= Gdx.graphics.getWidth()|| getX()<=0){
            reflectionWall();
            setCenterX(getX()<=Gdx.graphics.getWidth()/2? ballCircle.radius:Gdx.graphics.getWidth()- ballCircle.radius);
        }

        if ( getTop() >= GameField.topY){
            reflectionCeiling();
            setCenterY(GameField.topY- ballCircle.radius);
        }
        if (getY()<=GameField.bottomY) {
            AllBalls.startX = getCenterX();
            setCenterY(AllBalls.startY);
            motion = false;
        }


    }

    public void testDraw(SpriteBatch batch){
        sprite.setX(getBottomX());
        sprite.setY(getBottomY());
        sprite.draw(batch);
    }

    public void normalizeVelocity(float x,float y){
        float width = x-getCenterX();
        float height = y-getCenterY();
        float index =(float)(maxVelocity/Math.hypot(width,height));
        velocityX = width*index;
        velocityY = height*index;
    }

    public void launch(float x,float y){
        normalizeVelocity(x, y);
        motion=true;
        //Log.d("myteg","x:"+velocityX+" y:"+velocityY);

    }
    public void moveTo(float x, float y) {
        if (getCenterX() != x || getCenterY()!= y) {
            if (Math.hypot(getCenterX()-x,getCenterY()-y) < Math.hypot(maxVelocity,maxVelocity)) {
                setCenterX(x);
                setCenterY(y);
            } else {
                normalizeVelocity(x, y);

                moveBy( velocityX*GameField.coefficientFps,velocityY*GameField.coefficientFps);
            }
            System.out.println("moveTo");
        }

    }
    public void dispose () {

    }
}
