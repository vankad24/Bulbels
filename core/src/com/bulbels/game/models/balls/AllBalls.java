package com.bulbels.game.models.balls;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.bulbels.game.screens.GameField;

import java.util.Random;


public class AllBalls {
    public static float radius,startX, startY, maxVelocity, stepGrowth, size;
    public static int damage=1;
    static Array<Ball> balls;
    public AllBalls() {
        balls = new Array<>();

        radius = 15* GameField.coefficientWidth;
        maxVelocity =30*GameField.coefficientWidth;
        size =  radius;
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

    public void createBall(float x,float y){ balls.add(new Ball(x,y));}
    public void addBall(){ balls.add(new Ball(startX,startY));}
    public void setX(float x) {
        for (Ball b : balls) {
            b.setX(x);
        }
    }

    public void setY(float y){
        for (Ball b: balls){
            b.setY(y);
        }
    }

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
            if (x!=b.getX()||y!=b.getY()){
                //System.out.println("x,y"+x+" "+y+" b "+b.getX()+" "+b.getY());
                return false;
            }
        }
        return true;
    }
    public void drawBalls(SpriteBatch batch){

        float lastX = -1, lastY = -1;
        for (Ball ball : balls) {
            if (lastX != ball.getBottomX() || lastY != ball.getBottomY()) {
                lastX = ball.getBottomX();
                lastY = ball.getBottomY();
                ball.spriteBall.setY(ball.getBottomY());
                ball.spriteBall.setX(ball.getBottomX());
                ball.spriteBall.draw(batch);
            }
        }

    }
    public void dispose(){
        for (Ball ball : balls) {
            ball.dispose();
        }
    }

    static void delete(Ball ball){
        balls.removeValue(ball,true);
    }

    public static void addSpecialBalls(int quantity, float x, float y){
        Random rand = new Random();
        for (int i = 0; i <quantity ; i++) {
            balls.add(new SpecialBall(x,y,rand.nextInt(100)-50,rand.nextInt(100)-50));
        }
    }

    public void setMaxVelocity(float vel){
        for (Ball ball : balls) {
            ball.maxVelocity=vel;
        }
    }
    public void resize(float newSize){
        stepGrowth = (newSize-size)/6f;
        size = newSize;
        for (Ball ball : balls) ball.resizing = true;
    }

    public void setNormalSize(){
        for (Ball ball : balls){
            ball.ballCircle.radius=AllBalls.radius;
            ball.spriteBall.setSize(AllBalls.radius,size);
        }
    }
}
