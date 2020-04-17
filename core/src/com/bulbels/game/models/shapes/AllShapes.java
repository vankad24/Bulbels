package com.bulbels.game.models.shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.bulbels.game.models.balls.AllBalls;
import com.bulbels.game.models.balls.Ball;
import com.bulbels.game.screens.GameField;

import java.util.Random;

public class AllShapes {
    public static TextureAtlas textureAtlas;
    static BitmapFont font;
    static Shape[][] shapes;
    int[] chances={60,20,20,10};
    public static float width;
    float space, margin;
    int shapesInWidth = 6, shapesInHeight = 5;
    public AllShapes(){
        font = new BitmapFont(Gdx.files.internal("myFont.fnt"));
        font.setColor(Color.WHITE);
        font.getData().setScale(GameField.coefficientWidth);
        margin = AllBalls.radius*3;
        space = AllBalls.radius;
        width = (Gdx.graphics.getWidth() - margin*2 - space*shapesInWidth)/ shapesInWidth;
        shapes = new Shape[shapesInHeight][shapesInWidth];
    }

    static void delete(Shape shape){
        for (int i = 0; i < shapes.length; i++) {
            for (int j = 0; j < shapes[0].length; j++) {
                if (shape == shapes[i][j]){
                    shapes[i][j]=null;
                    return;
                }
            }
        }
    }

    static int dropWithChance(int[] arrChances){
        Random rand = new Random();
        int chance =0,sum=0;
        for (int x:arrChances)sum+=x;
        int num = rand.nextInt(sum);
        for (int i = 0; i < arrChances.length-1; i++) {
            chance += arrChances[i];
            if (num<chance)return i;
        }
        return arrChances.length-1;
    }

    public void generateNewLine(){
        for (int i = shapes.length-1; i >0 ; i--)
            for (int j = 0; j < shapes[0].length; j++) {
                shapes[i][j] = shapes[i - 1][j];
                if (shapes[i][j]!=null)shapes[i][j].addY(-width-space);
            }
        boolean isAir=true;
        while (isAir){
            for (int i = 0; i <shapes[0].length ; i++) {
                int drop = dropWithChance(chances);
                if (drop!=0)isAir=false;
                switch (drop){
                    case 0:
                        shapes[0][i]=null;
                        break;
                    case 1:
                        shapes[0][i]=new Square(margin+(width+space)*i,GameField.topY-margin-width,GameField.turn*2);
                        break;
                    case 2:
                        shapes[0][i]=new MyCircle(margin+(width+space)*i,GameField.topY-margin-width,GameField.turn*2);
                        break;
                    case 3:
                        shapes[0][i]=new Box(margin+(width+space)*i,GameField.topY-margin-width,GameField.turn*2);
                        break;
                }
            }
        }
    }

    public void draw(SpriteBatch batch){
        for (Shape[] s: shapes){
            for (Shape shape: s){
                if (shape!=null)shape.drawShape(batch);
            }
        }
    }

    public void checkCollision(Ball ball) {
        if (ball.inMotion()) {
            for (Shape[] s : shapes) {
                for (Shape shape : s) {
                    if (shape != null && !shape.destroying) shape.checkCollision(ball);
                }
            }
        }
    }
}
