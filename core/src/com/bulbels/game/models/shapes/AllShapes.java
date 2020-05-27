package com.bulbels.game.models.shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pool;
import com.bulbels.game.Bulbels;
import com.bulbels.game.models.MyActor;
import com.bulbels.game.models.balls.AllBalls;
import com.bulbels.game.models.balls.Ball;
import com.bulbels.game.screens.GameField;
import com.bulbels.game.screens.ShopScreen;
import com.bulbels.game.utils.ShapeData;

import java.util.Random;
import java.util.logging.Logger;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.after;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.repeat;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sizeTo;
import static com.bulbels.game.Bulbels.coefficientHeight;

public class AllShapes extends Group {
    public static TextureAtlas textureAtlas;
    static BitmapFont font;
    private GameField gameField;
    public Shape[][] shapes;
    int[] chances = {60, 40, 10, 10, 3};
    public static float width;
    float space, margin;
    int shapesInWidth = 6, shapesInHeight ;
    MyActor warning;
    Random rand = new Random();
    public Pool<Square> squarePool = new Pool<Square>() {
        @Override
        protected Square newObject() {
            return new Square(gameField);
        }
    };

    Pool<Box> boxPool = new Pool<Box>() {
        @Override
        protected Box newObject() {
            return new Box(gameField);
        }
    };

    Pool<MyCircle> myCirclePool = new Pool<MyCircle>() {
        @Override
        protected MyCircle newObject() {
            return new MyCircle(gameField);
        }
    };

    public Pool<Coin> coinPool = new Pool<Coin>() {
        @Override
        protected Coin newObject() {
            return new Coin(gameField);
        }
    };

    public AllShapes(GameField gameField) {
        this.gameField = gameField;
        textureAtlas = gameField.atlas;
        font = new BitmapFont(Gdx.files.internal("myFont.fnt"));
        font.setColor(Color.WHITE);
        font.getData().setScale(.5f * Bulbels.coefficientWidth);
        margin = AllBalls.radius * 3;
        space = AllBalls.radius;
        width = (Gdx.graphics.getWidth() - margin * 2 - space * shapesInWidth) / shapesInWidth;
        shapesInHeight = (int)((GameField.topY-GameField.bottomY-margin)/(width+space));
        System.out.println(shapesInHeight+" shapesInHeight");
        warning = new MyActor(gameField.atlas.findRegion("square"),0,GameField.topY-margin-(shapesInHeight-1)*(width+space),Gdx.graphics.getWidth(),5*coefficientHeight);
        warning.setColor(Color.RED);
        warning.setVisible(false);
        initialize();

    }
    public void initialize(){
        shapes = new Shape[shapesInHeight][shapesInWidth];
        clearChildren();
        warning.clearActions();
        warning.addAction(alpha(0));
        addActor(warning);
    }

    void delete(Shape shape) {
        Class aClass = shape.getClass();
        if (aClass==Square.class)squarePool.free((Square) shape);
        else if (aClass==MyCircle.class)myCirclePool.free((MyCircle) shape);
        else if (aClass==Coin.class)coinPool.free((Coin) shape);
        else if (aClass==Box.class)boxPool.free((Box) shape);
        shape.remove();
        for (int i = 0; i < shapes.length; i++) {
            for (int j = 0; j < shapes[0].length; j++) {
                if (shape == shapes[i][j]) {
                    shapes[i][j] = null;
                    return;
                }
            }
        }
    }

    @Override
    public void act(float delta) {
        if (!gameField.stop)super.act(delta);
    }

    void switchShapes(Shape first, Shape second) {
        for (int i = 0; i < shapes.length; i++) {
            for (int j = 0; j < shapes[0].length; j++) {
                if (first == shapes[i][j]) {
                    shapes[i][j] = second;
                    addActor(second);
                }
            }
        }
    }

    static int dropWithChance(int[] arrChances) {
        Random rand = new Random();
        int chance = 0, sum = 0;
        for (int x : arrChances) sum += x;
        int num = rand.nextInt(sum);
        for (int i = 0; i < arrChances.length - 1; i++) {
            chance += arrChances[i];
            if (num < chance) return i;
        }
        return arrChances.length - 1;
    }


    public void generateNewLine() {
        System.out.println("generateNewLine");
        for (int i = 0; i < shapes[0].length; i++)
            if (shapes[shapes.length - 1][i] != null)
                shapes[shapes.length - 1][i].remove();
        for (int i = shapes.length - 1; i > 0; i--)
            for (int j = 0; j < shapes[0].length; j++) {
                shapes[i][j] = shapes[i - 1][j];
                if (shapes[i][j] != null) shapes[i][j].moveBy(0, -width - space);
            }
        boolean isAir = true;
        while (isAir) {
            for (int i = 0; i < shapes[0].length; i++) {
                int drop = dropWithChance(chances);
                shapes[0][i] = null;
                switch (drop) {
                    case 0:
                        break;
                    case 1:
                        shapes[0][i] = squarePool.obtain();
                        break;
                    case 2:
                        shapes[0][i] = myCirclePool.obtain();
                        break;
                    case 3:
                        if (GameField.turn<0)continue;
                        shapes[0][i] = boxPool.obtain();
                        break;
                    case 4:
                        if (GameField.turn<0)continue;
                        shapes[0][i] = coinPool.obtain();
                        break;
                }
                if (shapes[0][i] != null) {
                    shapes[0][i].init(margin + (width + space) * i, GameField.topY - margin - width, GameField.turn);
                    if (shapes[0][i].getClass()==Coin.class)addActor(shapes[0][i]);
                    else addActorAt(0,shapes[0][i]);
                    isAir = false;
                }
            }
            System.out.println("size "+getChildren().size);
        }
        checkLose();
    }
    public void checkLose(){
        for (int i = 1; i < 3; i++) {
            for (int j = 0; j < shapes[0].length; j++) {
                Shape s = shapes[shapesInHeight - i][j];
                if ( s!= null&&!s.destroying && s.getClass()!=Coin.class && s.getClass()!=Box.class) {
                    if (i == 1) {
                        gameField.lose();
                        gameField.game.audioManager.lose.play(gameField.game.audioManager.getVolumeEffects());
                    } else if (!gameField.lose)warn();
                    break;
                }
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (Shape[] s : shapes) {
            for (Shape shape : s) {
                if (shape != null) {
                    shape.act(Gdx.graphics.getDeltaTime());
                    shape.draw(batch, 1);
                }
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

    public void addCoins(int amount, float x, float y){
        for (int i = 0; i <amount ; i++) {
            Coin coin = coinPool.obtain();
            coin.init(x+rand.nextInt((int)width/3)-width/3, y+rand.nextInt((int)width/3)-width/3,1);
            addActor(coin);
            coin.addAction(Actions.after(Actions.delay(i*.2f)));
            coin.destroying=true;
            coin.destroy(false);
        }
    }


    void addSkin(float x, float y){
        final Group group =new Group();
        group.setBounds(x,y,gameField.coins.getWidth(),gameField.coins.getWidth());
        Image background = new Image(gameField.atlas.findRegion("background"));
        background.setSize(gameField.coins.getWidth(),gameField.coins.getWidth());
        Image skins = new Image(gameField.atlas.findRegion("skins"));
        skins.setSize(gameField.coins.getWidth()*.9f,gameField.coins.getWidth()*.9f);
        skins.setPosition(gameField.coins.getWidth()/2,gameField.coins.getWidth()/2, Align.center);
        group.addActor(background);
        group.addActor(skins);
        addActor(group);
        Runnable remove = new Runnable() {
            @Override
            public void run() {
                gameField.game.audioManager.coin();
                group.remove();
            }
        };
        group.addAction(sequence(moveTo(gameField.coins.getX(),gameField.coins.getY(),2f, Interpolation.exp5In),run(remove)));
        ShopScreen.addSkin();
    }

    void addBoost(float x, float y){
        final Group group =new Group();
        group.setBounds(x,y,gameField.coins.getWidth(),gameField.coins.getWidth());
        Image background = new Image(gameField.atlas.findRegion("background"));
        background.setSize(gameField.coins.getWidth(),gameField.coins.getWidth());
        Image boost = new Image(gameField.atlas.findRegion("boosts"));
        boost.setSize(gameField.coins.getWidth()*.9f,gameField.coins.getWidth()*.9f);
        boost.setPosition(gameField.coins.getWidth()/2,gameField.coins.getWidth()/2, Align.center);
        group.addActor(background);
        group.addActor(boost);
        addActor(group);
        Runnable remove = new Runnable() {
            @Override
            public void run() {
                gameField.game.audioManager.coin();
                group.remove();
            }
        };
        group.addAction(sequence(moveTo(gameField.coins.getX(),gameField.coins.getY(),2f, Interpolation.exp5In),run(remove)));

        ShopScreen.addBoost();
    }

    public void warn(){
        warning.setVisible(true);
        gameField.game.audioManager.warning.play(gameField.game.audioManager.getVolumeEffects());
        Runnable hide = new Runnable() {
            @Override
            public void run() {
                warning.setVisible(false);
            }
        };
        warning.clearActions();
        warning.addAction(sequence(alpha(0),repeat(2,sequence(fadeIn(1f),fadeOut(1f))),run(hide)));
    }

    public ShapeData[][] getArrayData() {
        ShapeData[][] data = new ShapeData[shapes.length][shapes[0].length];
        for (int i = 0; i < shapes.length; i++) {
            for (int j = 0; j < shapes[0].length; j++) {
                if (shapes[i][j] == null || shapes[i][j].destroying) data[i][j] = null;
                else data[i][j] = shapes[i][j].getData();
            }
        }
        return data;
    }

    public void loadData(ShapeData[][] data) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                if (data[i][j] == null) shapes[i][j] = null;
                else {
                    switch (data[i][j].type) {
                        case 1:
                            shapes[i][j] = new Square(gameField);
                            break;
                        case 2:
                            shapes[i][j] = new MyCircle(gameField);
                            break;
                        case 3:
                            shapes[i][j] = new Box(gameField);
                            break;
                        case 4:
                            shapes[i][j] = new Coin(gameField);
                            break;
                    }
                    shapes[i][j].init(data[i][j].x, data[i][j].y, data[i][j].health);
                    shapes[i][j].setMaxHealth(data[i][j].maxHealth);
                    addActor(shapes[i][j]);
                }
            }
        }
    }
}