package com.bulbels.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.bulbels.game.Bulbels;
import com.bulbels.game.utils.AndroidHelper;
import com.bulbels.game.utils.MyInput;


public class MainMenu implements MyScreen {
    Skin skin;
    Stage stage;
    TextButton startButton;
    final Bulbels game;
    OrthographicCamera camera;
    AndroidHelper androidHelper;
    long lastBack;
    public MainMenu(final Bulbels game) {
        this.game = game;
        androidHelper = game.androidHelper;
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        skin =new Skin(game.atlas);
        skin.load(Gdx.files.internal("skins/button_skin.json"));
        startButton = new TextButton("Играть!",skin,"default");
        stage = new Stage();
        startButton.setX(Gdx.graphics.getWidth()/2f);
        startButton.setY(Gdx.graphics.getHeight()/2f);
        startButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                GameField gameField =new GameField(game);
                MyInput input = new MyInput();
                input.field = gameField;
                Gdx.input.setInputProcessor(input);
                game.add(gameField);
                System.out.println("touch");
            }
        });
        stage.addActor(startButton);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0.5f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.draw(game.batch,"Hello!",Gdx.graphics.getWidth()/2f,Gdx.graphics.getHeight()/2f);
        game.batch.end();

        stage.act(delta);
        stage.draw();
        if (Gdx.input.isTouched()){
            System.out.println("old_touch");
        }
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
        System.out.println("dispose");
    }

    @Override
    public void back() {
        if (System.currentTimeMillis()-lastBack<2000){
            game.remove();
            game.androidHelper.finish();
        }else {
            game.androidHelper.makeToast("Нажмите снова, чтобы выйти");
            lastBack = System.currentTimeMillis();
        }
    }
}
