package com.bulbels.game.models.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.bulbels.game.models.balls.AllBalls;
import com.bulbels.game.models.balls.Ball;
import com.bulbels.game.screens.GameField;
import com.bulbels.game.utils.ShapeData;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.bulbels.game.Bulbels.coefficientWidth;
import static com.bulbels.game.models.shapes.AllShapes.*;

public class Box extends Square {



    Box(GameField field) {
        super("box", field);
        //sprite.setColor(1f,150/255f,0,1);

    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setScale(getScaleX(), getScaleY());
        sprite.setPosition(getX(), getY());
        sprite.setAlpha(getColor().a);
        sprite.draw(batch);
        if (!destroying) {
            glyphLayout.setText(font, health+"");
            font.draw(batch, glyphLayout, rectangle.x + (width - glyphLayout.width) / 2, rectangle.y + (width + glyphLayout.height) / 2);
        }
    }


    @Override
    public void checkCollision(Ball ball) {
        this.ball = ball;
        if (Intersector.overlaps(ball.ballCircle, rectangle)) {

            if (!ball.ghost)reflection();
            health-= AllBalls.damage;
            if (health <= 0) {
                destroying = true;
                destroy();
                drop();
            }
        }

    }

    void drop(){
        int[] chances={60,40,10,3};
        switch (dropWithChance(chances)) {
            case 0:
                field.allBalls.addSpecialBalls(5,rectangle.x+width/2f,rectangle.y+width/2f);
                break;
            case 1:
                Square square = field.allShapes.squarePool.obtain();
                square.init(getX(),getY(),GameField.turn*2);
                square.addAction(sequence(scaleTo(.5f,.5f),parallel(fadeIn(1),scaleTo(1,1,1))));
                field.allShapes.switchShapes(this,square);
                break;
            case 2:
                field.allShapes.addCoins(5,getX(), getY());
                break;
            case 3:
                int[] chances2={70,30};
                switch (dropWithChance(chances2)){
                    case 0:
                        field.allShapes.addBoost(getX(),getY());
                        break;
                    case 1:
                        field.allShapes.addSkin(getX(),getY());
                        break;
                }
                break;
        }
    }

    @Override
    public void destroy() {

        end = new Runnable() {
            @Override
            public void run() {
                 field.game.audioManager.crash_box();
                 delete();
            }
        };
        addAction(after(sequence(run(end),removeActor())));


    }

    @Override
    public ShapeData getData() { return new ShapeData(3,health,maxHealth,getX(),getY()); }
}
