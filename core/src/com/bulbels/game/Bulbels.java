package com.bulbels.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.bulbels.game.models.TextureManager;
import com.bulbels.game.models.balls.Skin;
import com.bulbels.game.screens.MainMenu;
import com.bulbels.game.screens.MyScreen;
import com.bulbels.game.screens.SplashScreen;
import com.bulbels.game.utils.ScreenManager;
import com.bulbels.game.utils.AndroidHelper;
import com.bulbels.game.utils.AudioManager;
import com.bulbels.game.utils.Settings;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class Bulbels extends Game implements ScreenManager {
    public static float coefficientWidth, coefficientHeight;
    public static Skin skin;
    public int money, indexOfColor, record;
    public Color backgroundColor, second_backgroundColor;
    public AudioManager audioManager;
    public Settings settings;
    public SpriteBatch batch;
    public BitmapFont font;
    public AndroidHelper androidHelper;
    public TextureManager textureManager;
    public TextureAtlas atlas;
    public boolean hint=true;
    Stage stage;

    public Bulbels(AndroidHelper androidHelper) {
        this.androidHelper = androidHelper;

    }

    @Override
    public void create() {
        textureManager = new TextureManager();
        atlas =textureManager.getManager().get("myAtlas.atlas");
        batch = new SpriteBatch();
        font =new BitmapFont(Gdx.files.internal("myFont.fnt"));
        stage = new Stage();
        coefficientWidth=Gdx.graphics.getWidth()/720f;
        coefficientHeight = Gdx.graphics.getHeight()/1280f;
        font.getData().setScale(.5f*coefficientWidth);
        if (androidHelper==null){
            createDesktopHelper();
        }
        skin = new Skin(atlas);
        settings =new Settings(this);
        audioManager = new AudioManager();
        settings.loadGame(this);
        audioManager.music.setVolume(audioManager.getVolumeMusic());
        add(new SplashScreen(this));
        System.out.println(numberOfScreens());
    }


    @Override
    public MyScreen add(MyScreen screen) {
        setScreen(screen);
        return SCREENS.push(screen);
    }

    @Override
    public MyScreen remove() {
        if (!SCREENS.empty())SCREENS.pop().dispose();
        if (!SCREENS.empty())setScreen(SCREENS.peek());
        return SCREENS.empty()?null:SCREENS.peek();
    }

    @Override
    public void back() {
        SCREENS.peek().back();
    }

    @Override
    public void dispose() {
        getScreen().dispose();
        settings.saveGame(this);
        settings.save();
    }

    void createDesktopHelper(){
        androidHelper = new AndroidHelper() {

            @Override
            public void printMessage(String title, String text) {}

            @Override
            public void makeToast(String text) {
                Label.LabelStyle style = new Label.LabelStyle(new BitmapFont(Gdx.files.internal("myFont.fnt")),Color.WHITE);
                Sprite sprite = new Sprite(atlas.findRegion("square"));
                sprite.setColor(Color.BLACK);
                style.font.getData().setScale(.5f);
                style.background = new SpriteDrawable(sprite);
                Label toastLabel = new Label(text, style);
                toastLabel.setAlignment(Align.center);
                stage.addActor(toastLabel);
                toastLabel.setPosition((Gdx.graphics.getWidth()-toastLabel.getWidth())/2f,Gdx.graphics.getHeight()*.1f);
                toastLabel.addAction(sequence(fadeOut(3f),removeActor()));
            }

            @Override
            public String getString(String name) {
                return STRINGS.get(name);
            }
        };

        androidHelper.STRINGS.put("exit","Выход");
        androidHelper.STRINGS.put("confirmExit","Нажмите снова, чтобы выйти");
        androidHelper.STRINGS.put("balls","Шариков");
        androidHelper.STRINGS.put("play","Играть");
        androidHelper.STRINGS.put("settings","Настройки");
        androidHelper.STRINGS.put("turn","Ход");
        androidHelper.STRINGS.put("shop","Магазин");
        androidHelper.STRINGS.put("bought","Куплено");
        androidHelper.STRINGS.put("chosen","Выбрано");
        androidHelper.STRINGS.put("effect_volume","Громкость эффектов");
        androidHelper.STRINGS.put("music_volume","Громкость музыки");
        androidHelper.STRINGS.put("general_volume","Общая громкость");
        androidHelper.STRINGS.put("resume","Продолжить");
        androidHelper.STRINGS.put("menu","Меню");
        androidHelper.STRINGS.put("pause","Пауза");
        androidHelper.STRINGS.put("game_over","Конец игры");
        androidHelper.STRINGS.put("restart","Заново");
        androidHelper.STRINGS.put("score","Счет");
    }
    public int numberOfScreens(){return SCREENS.size();}

    @Override
    public void render() {
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();

        stage.act();
        stage.draw();
    }
}
