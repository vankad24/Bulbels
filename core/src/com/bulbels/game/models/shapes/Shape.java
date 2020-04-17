package com.bulbels.game.models.shapes;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bulbels.game.models.balls.AllBalls;
import com.bulbels.game.models.balls.Ball;

abstract public class Shape {
    int health, maxHealth, steps, alpha=255;
    BitmapFont font;
    float stepGrowth;
    protected boolean destroying;
    Ball ball;
    Sprite sprite, test;
    int[] RGB=new int[3];
    Vector2 main;
    abstract public void setX(float x);
    abstract public void setY(float y);
    abstract public void addY(float addY);
    abstract public void addX(float addX);
    public void destroy(){
        AllShapes.delete(this);
    }

    abstract public void drawShape(SpriteBatch batch);
    abstract protected void reflection();
    abstract public void checkCollision(Ball ball);
    //abstract public void onDestroy();

    public boolean normalizeCollision(float x1, float y1, float x2, float y2){
        float ballX =ball.getX();
        float ballY =  ball.getY();
        Vector2 main2 = new Vector2(x2-x1,y2-y1);
        Vector2 left = new Vector2( ball.old_x-x1,ball.old_y-y1);
        Vector2 right = new Vector2( ball.getX()-x1,ball.getY()-y1);
        float product1 =main2.crs(left), product2 = main2.crs(right);
        if (product1>0&&product2<0 || product1<0&&product2>0 ){
            right.set(x1-ball.old_x,y1-ball.old_y);
            left.set(x2-ball.old_x,y2-ball.old_y);
            product1 = main.crs(left);
            product2 = main.crs(right);
            if (product1>0&&product2<0 || product1<0&&product2>0 ) {
                float ratio = Math.abs(product1 / product2);
                float px = (x1 + x2 * ratio) / (ratio + 1);
                float py = (y1 + y2 * ratio) / (ratio + 1);
                test.setX(px);
                test.setY(py);
            /* float len = (float) Math.hypot(ball.getX() - px, ball.getY() - py);
                ratio = (float) AllBalls.radius / len;
                ball.setX(px - (ball.getX() - px) * ratio);
                ball.setY(py - (ball.getY() - py) * ratio);*/
                right.set(ball.old_x-px,ball.old_y-py);
                float len =AllBalls.radius / Math.abs(MathUtils.sinDeg(right.angle(main2)));
                right.setLength(len);
                ball.setX(px+right.x);
                ball.setY(py+right.y);

                return true;
            }
        }
        return false;
    }

    // всего 306 цветов
    public int[] convertToColor(int color){
        int[] RGB=new int[3];
        RGB[1] = 255;
        boolean plus = true;
        int m = 0;
        for (int i = 0; i < color /51; i++) {
            if (plus) RGB[m] = 255;
            else RGB[m] = 0;
            m = (m + 1) % 3;
            plus = !plus;
        }
        if (plus) RGB[m] = (color%51)*5;
        else RGB[m] = 255 - (color%51)*5;
        return RGB;
    }

}

