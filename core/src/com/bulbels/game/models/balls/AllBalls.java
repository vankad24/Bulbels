package com.bulbels.game.models.balls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.bulbels.game.Bulbels;

import java.util.Random;


public class AllBalls extends Group {
    public static float radius,startX, startY, maxVelocity, stepGrowth;
    public static int damage=1;
    static Array<Ball> balls;
    public AllBalls() {
        initialize();
        radius = 15* Bulbels.coefficientWidth;
        maxVelocity =30*Bulbels.coefficientWidth;

    }

    public void initialize(){
        balls = new Array<>();
        clearChildren();
    }

    public Array<Ball> getArrayBalls() {
        return balls;
    }
    public float getRadius(){
        return radius;
    }
    public void moveAll(){
        for (Ball b: balls){
            if (b.inMotion()) b.move();
        }
    }

    public void addBall(float x,float y) {
        Ball ball = new Ball(x, y);
        balls.add(ball);
        addActor(ball);
    }
    public void addBall(){ addBall(startX,startY);}
    public void addBall(int amount){for (int i = 0; i <amount ; i++)addBall(startX,startY);}

   /* public void setX(float x) {
        for (Ball b : balls) {
            b.setX(x);
        }
    }

    public void setY(float y){
        for (Ball b: balls){
            b.setY(y);
        }
    }*/

    public boolean inMotion(){
        for (Ball b: balls){
            if (b.inMotion())return true;
        }
        return false;
    }

    static boolean isLastInMotion(){
        boolean one=false;
        for (int i = balls.size-1; i >=0 ; i--) {
            if (balls.get(i)!=null && balls.get(i).inMotion()){
                if (one)return false;
                one =true;
            }
        }
        return one;
    }

    public void moveWithAnimationTo(float x,float y){
        for (Ball b: balls){
            b.moveTo(x, y);
        }
    }
    public boolean allIn(float x,float y){
        for (Ball b: balls){
            if (x!=b.getCenterX()||y!=b.getCenterY()){
                //System.out.println("x,y"+x+" "+y+" b "+b.getX()+" "+b.getY());
                return false;
            }
        }
        System.out.println("allIn");
        return true;
    }

    public void setSprites(Sprite sprite){for (Ball b: balls)b.setSprite(sprite); }

    public void drawBalls(SpriteBatch batch){

        float lastX = -1, lastY = -1;
        for (Ball ball : balls) {
            if (lastX != ball.getBottomX() || lastY != ball.getBottomY()) {
                lastX = ball.getBottomX();
                lastY = ball.getBottomY();
                ball.sprite.setY(ball.getBottomY());
                ball.sprite.setX(ball.getBottomX());
                ball.sprite.draw(batch);
            }
        }
    }

    public void print(){
        for (Ball ball : balls) System.out.println(ball.getX()+" "+ball.sprite.getX()+" "+ball.ballCircle.x+" "+ball.ballCircle.radius);
    }

    public void update(){
        for (Ball ball : balls)
            ball.positionChanged();
        }
    public void dispose(){
        for (Ball ball : balls) {
            ball.dispose();
        }
    }

    static void delete(Ball ball){
        balls.removeValue(ball,true);
        ball.remove();
    }

    public void addSpecialBalls(int quantity, float x, float y){
        Random rand = new Random();
        for (int i = 0; i <quantity ; i++) {
            SpecialBall newBall = new SpecialBall(x,y,x+rand.nextInt(100)-50,y+rand.nextInt(100)-50);
//            System.out.println(newBall.velocityX+" "+newBall.velocityY);
            balls.add(newBall);
            addActor(newBall);
        }
    }

    public static void resize(float newSize){
        for (Ball ball : balls)ball.setSize(newSize,newSize);
    }


}
