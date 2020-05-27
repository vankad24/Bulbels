package com.bulbels.game.models.shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.TimeScaleAction;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pool;
import com.bulbels.game.models.balls.AllBalls;
import com.bulbels.game.models.balls.Ball;
import com.bulbels.game.screens.GameField;
import com.bulbels.game.utils.ShapeData;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.bulbels.game.Bulbels.coefficientWidth;
import static com.bulbels.game.models.shapes.AllShapes.textureAtlas;
import static com.bulbels.game.models.shapes.AllShapes.width;

abstract public class Shape extends Actor implements Pool.Poolable {
    int health, maxHealth;
    BitmapFont font;
    GlyphLayout glyphLayout;
    public float duration=.7f;
    protected boolean destroying;
    Ball ball;
    GameField field;
    Sprite sprite, test;
    int[] RGB;
    Vector2 main;

    Shape(String textureName, GameField field){
        this.field = field;
        font = AllShapes.font;
        sprite = new Sprite(textureAtlas.findRegion(textureName));
        createShape();
    }

    public void delete(){
        field.allShapes.delete(Shape.this);
    }

    private Runnable destroy = new Runnable() {
        @Override
        public void run() {
            delete();
        }
    };
    //abstract public void setX(float x);

    public void destroy(){
        addAction(after(sequence(parallel(fadeOut(duration),scaleTo(1.3f,1.3f,duration)),run(destroy),removeActor())));
    }

    public void init(float x, float y, int health){
        this.health = health;
        this.maxHealth=health;
        sprite.setSize(width,width);
        sprite.setOriginCenter();
        setSize(width,width);
        setPosition(x,y);
        setOrigin(Align.center);
        updateColor(health);
        setZIndex(0);
        addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println(Shape.this.getClass().getSimpleName()+" "+getX()+" "+getY()+" "+getColor().a);
            }
        });
        glyphLayout = new GlyphLayout(font,health+"");
    }
    abstract public void createShape();
    abstract protected void reflection();
    abstract public void checkCollision(Ball ball);
    abstract public ShapeData getData();
    //abstract public void onDestroy();

    public boolean normalizeCollision(float x1, float y1, float x2, float y2){

        Vector2 main2 = new Vector2(x2-x1,y2-y1);
        Vector2 left = new Vector2( ball.old_x-x1,ball.old_y-y1);
        Vector2 right = new Vector2( ball.getCenterX()-x1,ball.getCenterY()-y1);
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
               // test.setX(px);
                //test.setY(py);
            /* float len = (float) Math.hypot(ball.getX() - px, ball.getY() - py);
                ratio = (float) AllBalls.radius / len;
                ball.setX(px - (ball.getX() - px) * ratio);
                ball.setY(py - (ball.getY() - py) * ratio);*/
                right.set(ball.old_x-px,ball.old_y-py);
                float len =AllBalls.radius / Math.abs(MathUtils.sinDeg(right.angle(main2)));
                right.setLength(len);
                ball.setCenterX(px+right.x);
                ball.setCenterY(py+right.y);

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

    public void updateColor(int health){
        if (maxHealth<260) RGB = convertToColor(health);
        else RGB = convertToColor((int) ((float)health/maxHealth*260));
        setColor(RGB[0] / 255f, RGB[1] / 255f, RGB[2] / 255f, getColor().a);
    }

    public void setMaxHealth(int maxHealth){
        this.maxHealth =maxHealth;
        updateColor(health);
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println(this.getClass().getSimpleName()+" deleted!");
    }

    @Override
    public void reset() {
        destroying=false;
        getColor().a =1;
        setScale(1);
        clear();
    }
}

