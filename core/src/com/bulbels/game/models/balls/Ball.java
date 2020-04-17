package com.bulbels.game.models.balls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.bulbels.game.models.shapes.AllShapes;
import com.bulbels.game.screens.GameField;

public class Ball {

    float velocityX,velocityY;
    public float maxVelocity = 30*GameField.coefficientWidth, old_x, old_y;
    boolean motion, resizing;
    public boolean ghost;

    public Sprite spriteBall;
    public Circle ballCircle;
    float diameter;
    public Ball() {this(0,0);}
    public Ball(float x, float y) {
        diameter = AllBalls.radius*2;
        spriteBall = new Sprite(AllShapes.textureAtlas.findRegion("ball"));
        spriteBall.setSize(diameter,diameter);
//        spriteBall.setColor((float) Math.random(),(float) Math.random(),(float) Math.random(),1);
        ballCircle= new Circle(x, y, diameter /2f);
    }




    public float getBottomX(){return ballCircle.x-ballCircle.radius;}//Только для рисования
    public float getBottomY(){return ballCircle.y-ballCircle.radius;}//Только для рисования
    public float getX(){return ballCircle.x;}
    public float getY(){return ballCircle.y;}
    public void setX(float x){ballCircle.x=x;}
    public void setY(float y){ballCircle.y=y;}
    public void reflectionWall(){ velocityX*=-1;}
    public void reflectionCeiling(){velocityY*=-1;}

    public boolean inMotion(){
        return motion;
    }

    public void move(){
        old_x=getX();
        old_y=getY();
        ballCircle.x+=velocityX*GameField.coefficientFps;
        ballCircle.y+=velocityY*GameField.coefficientFps;
        if (getX()+ ballCircle.radius >= Gdx.graphics.getWidth()|| getX()-ballCircle.radius<=0){
            reflectionWall();
            setX(getX()<=Gdx.graphics.getWidth()/2? diameter /2:Gdx.graphics.getWidth()- diameter /2);
        }

        if ( getY()+ ballCircle.radius >= GameField.topY){
            reflectionCeiling();
            setY(GameField.topY- ballCircle.radius);
        }
        if (getY()-ballCircle.radius<=GameField.bottomY) {
            AllBalls.startX = getX();
            AllBalls.startY=GameField.bottomY+ ballCircle.radius;
            setY(GameField.bottomY+ ballCircle.radius);
            motion = false;
        }
        if (resizing)resize();

    }

    void resize(){
        if (Math.abs(AllBalls.size-ballCircle.radius)<Math.abs(AllBalls.stepGrowth)){
            ballCircle.radius=AllBalls.size;
            spriteBall.setSize(ballCircle.radius*2,ballCircle.radius*2);
            resizing=false;
        }else{
            spriteBall.setSize((ballCircle.radius+=AllBalls.stepGrowth)*2,ballCircle.radius*2);
        }
    }
    public void testDraw(SpriteBatch batch){
        spriteBall.setX(getBottomX());
        spriteBall.setY(getBottomY());
        spriteBall.draw(batch);
    }

    public void checkCollision(Rectangle rectangle){
        if (Intersector.overlaps(ballCircle, rectangle)){
            spriteBall.setColor(Color.RED);
            System.out.println("Collision!!!");
        }else spriteBall.setColor(Color.GREEN);
    }

    public void normalizeVelocity(float x,float y){
        float width = x-ballCircle.x;
        float height = y-ballCircle.y;
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
        if (getX() != x || getY()!= y) {
            if (Math.hypot(getX()-x,getY()-y) < Math.hypot(maxVelocity,maxVelocity)) {
                setX(x);
                setY(y);
            } else {
                normalizeVelocity(x, y);
                ballCircle.x += velocityX*GameField.coefficientFps;
                ballCircle.y += velocityY*GameField.coefficientFps;
            }
        }

    }
    public void dispose () {


    }
}
