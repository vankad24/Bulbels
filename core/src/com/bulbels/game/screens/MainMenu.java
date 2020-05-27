package com.bulbels.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.TextureArray;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.bulbels.game.Bulbels;
import com.bulbels.game.models.MyActor;
import com.bulbels.game.models.shapes.AllShapes;
import com.bulbels.game.utils.AndroidHelper;
import com.bulbels.game.utils.ButtonPanel;
import com.bulbels.game.utils.Settings;
import com.google.gson.Gson;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.bulbels.game.Bulbels.coefficientHeight;
import static com.bulbels.game.Bulbels.coefficientWidth;


public class MainMenu extends MyScreen{

    class MyListener extends ClickListener {
        MyScreen myScreen;
        int slideX, slideY;
        public MyListener(MyScreen myScreen, int slideX, int slideY) {
            this.myScreen = myScreen;
            this.slideX = slideX;
            this.slideY = slideY;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            game.audioManager.click();
            if (myScreen==null){
                game.remove();
                Gdx.app.exit();
                return;
            }
            Runnable nextScreen = new Runnable() {
                @Override
                public void run() {
                    game.add(myScreen);
                    myScreen.slideStart();
                }
            };
                MainMenu.this.slideX = slideX;
                MainMenu.this.slideY = slideY;

            slideStart();
            stage.addAction(after(run(nextScreen)));
//            stage.addAction((sequence(delay(.6f),run(nextScreen))));
        }
    }


    TextButton startButton, settingsButton, exitButton;
    OrthographicCamera camera;
    AndroidHelper androidHelper;
    Label labelRecord;
    MyActor record;
    long lastBack;
    int slideX, slideY;
    GameField field;
    ShopScreen shopScreen;
    SettingsScreen settingsScreen;

    public MainMenu(final Bulbels game) {
        super(game);
        androidHelper = game.androidHelper;
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
//        skin =new Skin(Gdx.files.internal("skins/button_skin.json"));
        float buttonWidth = 450* coefficientWidth;
        float buttonHeight = 80* coefficientHeight;
        settingsScreen = new SettingsScreen(game);
        shopScreen = new ShopScreen(game);
//        startButton =createButton(buttonWidth, buttonHeight,, new Color(0.3f,1,1,1),"button_up", "button_down");
        startButton = new TextButton(androidHelper.getString("play")+"!", new Skin(Gdx.files.internal("skins/button_skin.json"),game.atlas),"default");
        startButton.setSize(buttonWidth,buttonHeight);
        startButton.getStyle().font.getData().setScale(startButton.getHeight()/80*.8f);
        startButton.setColor(new Color(.3f,1,1,1));
        field = new GameField(game,settingsScreen);
        startButton.addListener(new MyListener(field,0,-1));

        table.setX((Gdx.graphics.getWidth())/2f);
        table.align(Align.center|Align.top);
        float bound = Gdx.graphics.getHeight()*.5f;
        startButton.addAction(sequence(alpha(0),delay(.3f), parallel(fadeIn(.6f), moveBy(0, bound, .6f, Interpolation.pow3InInverse))));

        table.add(startButton).width(startButton.getWidth()).height(startButton.getHeight()).padBottom(150* coefficientHeight).colspan(3).row();

        //table.add(settingsButton).width(settingsButton.getWidth()).height(settingsButton.getHeight()).padTop(40).row();
       // table.add(exitButton).width(exitButton.getWidth()).height(exitButton.getHeight()).padTop(40).row();

        Group settings =createRoundButton(100* coefficientWidth, game.androidHelper.getString("settings"), Color.BLUE,"settings");
        settings.addAction(sequence(alpha(0), delay(.5f),parallel(fadeIn(.6f), moveBy(0, bound, .6f, Interpolation.pow3InInverse))));
        settings.addListener(new MyListener(settingsScreen,-1,0));

        Group shop =createRoundButton(100* coefficientWidth,game.androidHelper.getString("shop"),new Color(102/255f,0,236/255f,1) ,"shop");
        shop.addAction(sequence(alpha(0), delay(.7f),parallel(fadeIn(.6f), moveBy(0, bound, .6f, Interpolation.pow3InInverse))));
        shop.addListener(new MyListener(shopScreen,0,1));

        Group exit =createRoundButton(100* coefficientWidth,game.androidHelper.getString("exit"), Color.RED,"exit");
        exit.addAction(sequence(alpha(0), delay(.9f),parallel(fadeIn(.6f), moveBy(0, bound, .6f, Interpolation.pow3InInverse))));
        exit.addListener(new MyListener(null, 0, 0));

        /*Group info =createRoundButton(50,game.androidHelper.getString("exit"), Color.SKY,"info");

        info.addListener(new MyListener(null, 0, 0));*/
        TextButton info = new TextButton("i",new Skin(Gdx.files.internal("skins/button_skin.json"), game.atlas));
        info.setSize(50* coefficientWidth,50* coefficientWidth);
        info.getStyle().up = new TextureRegionDrawable(game.atlas.findRegion("r_button_up"));
        info.getStyle().down = new TextureRegionDrawable(game.atlas.findRegion("r_button_down"));
        info.getLabel().setFontScale(.8f* coefficientWidth);
        info.setColor(Color.PURPLE);
        info.setPosition(40* coefficientWidth,Gdx.graphics.getHeight()-40* coefficientWidth-bound,Align.topLeft);
        info.addAction(sequence(alpha(0), delay(1.2f),parallel(fadeIn(.6f), moveBy(0, bound, .6f, Interpolation.swingOut))));
        info.addListener(new MyListener(new InfoScreen(game),1,0));

        labelRecord = new Label("999",new Label.LabelStyle(game.font,new Color(102/255f,0,236/255f,1) ));
        labelRecord.setScale(info.getWidth()/100);
        labelRecord.pack();
        labelRecord.setPosition(Gdx.graphics.getWidth()-20*coefficientHeight,Gdx.graphics.getHeight()-40* coefficientWidth-bound,Align.topRight);
        record = new MyActor(game.atlas.findRegion("record"),0,0,info.getWidth(),info.getHeight());
        record.setPosition(labelRecord.getX()-10*coefficientWidth,labelRecord.getY()+labelRecord.getHeight()/2,Align.center|Align.right);
        record.addAction(sequence(alpha(0), delay(1.4f),parallel(fadeIn(.6f), moveBy(0, bound, .6f, Interpolation.swingOut))));
        labelRecord.addAction(sequence(alpha(0), delay(1.4f),parallel(fadeIn(.6f), moveBy(0, bound, .6f, Interpolation.swingOut))));




        stage.addActor(labelRecord);
        stage.addActor(record);
        stage.addActor(info);

        TextureAtlas animationAtlas = game.textureManager.getManager().get("nameAtlas.atlas");
        final TextureRegion[] frames = new TextureRegion[animationAtlas.getRegions().size];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = animationAtlas.findRegion(i+1+"");
        }
        MyActor nameAnimation = new MyActor(null, 0,0,frames[0].getRegionWidth(),frames[0].getRegionHeight()){
            float stateTime;
            Animation<TextureRegion> animation = new Animation(.03f,frames);

            @Override
            public void draw(Batch batch, float parentAlpha) {
                stateTime +=Gdx.graphics.getDeltaTime();
                sprite =new Sprite(animation.getKeyFrame(stateTime,true));
                super.draw(batch, parentAlpha);
            }
        };

        nameAnimation.setSize(startButton.getWidth(),nameAnimation.getHeight()/nameAnimation.getWidth()*startButton.getWidth());
        nameAnimation.setPosition(Gdx.graphics.getWidth()/2f,bound*1.4f,Align.center);
        stage.addActor(nameAnimation);
        stage.addActor(table);
//        stage.setDebugAll(true);
    }


    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer(stage,this);
        Gdx.input.setInputProcessor(multiplexer);
        labelRecord.setText(game.record);
        settingsScreen.panes.get(game.indexOfColor).setColor();
    }

    @Override
    public void render(float delta) {

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        //game.font.draw(game.batch,"Hello!",Gdx.graphics.getWidth()/2f,Gdx.graphics.getHeight()/2f);
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }


    TextButton createButton(float width, float height, String text, Color color, String nameUp, String nameDown){
        TextButton button = new TextButton(text, new Skin(Gdx.files.internal("skins/button_skin.json")),"default");
        float darker = .3f;
        Sprite sprite = new Sprite(game.atlas.findRegion(nameUp));
        sprite.setColor(color);
        button.getStyle().up = new SpriteDrawable(sprite);
        Sprite sprite2 = new Sprite(game.atlas.findRegion(nameDown));
//        sprite2.setColor(color.r<darker?0:color.r-darker,color.g<darker?0:color.g-darker,color.b<darker?0:color.b-darker,1);
        sprite2.setColor(color);
        button.getStyle().down = new SpriteDrawable(sprite2);
        button.setSize(width,height);
        button.getStyle().font.getData().setScale(button.getHeight()/80*.8f);
        return button;
    }

    Group createRoundButton(final float diameter, String hint, Color color, String nameIcon){
        float coeff = diameter/300;
        Group group = new Group();
        group.setSize(diameter,diameter);
        Sprite sprite = new Sprite(game.atlas.findRegion("r_button_up"));
        Sprite sprite2 = new Sprite(game.atlas.findRegion("r_button_down"));
        sprite.setColor(color);
        sprite2.setColor(color);
        ImageButton button = new ImageButton(new SpriteDrawable(sprite), new SpriteDrawable(sprite2));
        button.setSize(diameter,diameter);
        final Label label = new Label(hint,new Skin(Gdx.files.internal("skins/label_skin.json")),"white");
        label.setFontScale(coeff);
        label.pack();
        //System.out.println(label.getPrefWidth());

        label.setPosition(diameter/2, diameter,Align.bottom|Align.center);
        label.setTouchable(Touchable.disabled);
        label.setVisible(false);

        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.audioManager.click();
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                label.setVisible(true);
                label.addAction(sequence(alpha(0), delay(.3f),alpha(.1f),
                        parallel(moveBy(0,label.getPrefHeight()/2* coefficientHeight,.7f),fadeIn(.7f))));
                cancel();
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                label.clearActions();
                label.setVisible(false);
                label.setPosition(diameter/2, diameter,Align.bottom|Align.center);
            }
        });

        Image image = new Image(game.atlas.findRegion(nameIcon));
        image.setOrigin(image.getWidth()/2,image.getHeight()/2);
        image.setScale(diameter/450f);
        image.setPosition(diameter/2,diameter/2, Align.center);
        image.setTouchable(Touchable.disabled);

        group.addActor(button);
        group.addActor(label);
        group.addActor(image);
        table.add(group);
//        System.out.println(group.getWidth()+" "+group.getHeight()+" "+button.getWidth()+" "+button.getHeight());
        return group;
    }
    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        game.atlas.dispose();
        AllShapes.textureAtlas.dispose();
        System.out.println("dispose");
    }

    @Override
    public void back() {
        if (System.currentTimeMillis()-lastBack<2000){
            game.settings.saveField(field);//Todo перенести в dispose
            game.settings.saveShop(shopScreen);
            Gdx.app.exit();
            game.remove();
        }else {
            game.androidHelper.makeToast(androidHelper.getString("confirmExit"));
            lastBack = System.currentTimeMillis();
        }
    }

    @Override
    public void slideStart() {
        Runnable sound = new Runnable() {
            @Override
            public void run() {
                game.audioManager.slide();
            }
        };
        stage.addAction(parallel(moveBy(slideX*Gdx.graphics.getWidth(), slideY*Gdx.graphics.getHeight(), .8f, Interpolation.swingIn), sequence(delay(.5f),run(sound))));
    }

    @Override
    public void slideBack() {
        game.audioManager.slide();
        stage.addAction( moveBy(-slideX*Gdx.graphics.getWidth(), -slideY*Gdx.graphics.getHeight(), .8f, Interpolation.swingOut));

    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.BACKSPACE:
                game.settings.clear();
                System.out.println("clear");
                break;
        }
        return super.keyDown(keycode);
    }
}
