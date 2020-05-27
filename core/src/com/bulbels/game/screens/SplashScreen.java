package com.bulbels.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.bulbels.game.Bulbels;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveToAligned;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.bulbels.game.Bulbels.coefficientWidth;

public class SplashScreen extends MyScreen {
    Image logo, comp, lamp, libgdx;
    Texture texture, texture2, texture3, texture4;
    MainMenu mainMenu;
    public SplashScreen(final Bulbels game) {
        super(game);
        game.backgroundColor = Color.WHITE;
        game.second_backgroundColor = Color.WHITE;
        texture = new Texture(Gdx.files.internal("libgdx.png"));
        texture2 = new Texture(Gdx.files.internal("logo.png"));
        texture3 = new Texture(Gdx.files.internal("comp.png"));
        texture4 = new Texture(Gdx.files.internal("lamp.png"));
        logo = new Image(texture2);
        comp = new Image(texture3);
        lamp = new Image(texture4);
        libgdx = new Image(texture);

        comp.getColor().a = 0;
        lamp.getColor().a = 0;
        libgdx.getColor().a = 0;
        logo.getColor().a = 0;
        mainMenu =new MainMenu(SplashScreen.this.game);

        libgdx.setSize(Gdx.graphics.getWidth()*.5f,Gdx.graphics.getWidth()*.5f);
        libgdx.setPosition(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f, Align.center);
        libgdx.addAction(sequence(fadeIn(1.5f),delay(.3f),fadeOut(1f), run(new Runnable() {
            @Override
            public void run() {
                table.addActor(comp);
            }
        }), removeActor()));

        comp.setSize(Gdx.graphics.getWidth()*.5f,Gdx.graphics.getWidth()*.35f);
        comp.setPosition(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f, Align.center);
        comp.setOrigin(comp.getWidth()*.35f,comp.getHeight()/2f);
        comp.addAction(sequence(fadeIn(2f),scaleTo(3.5f,3.5f, 4f, Interpolation.pow2In), run(new Runnable() {
            @Override
            public void run() {
                table.addActor(lamp);

            }
        }),fadeOut(1f)));

        logo.setSize(Gdx.graphics.getWidth()*.5f,Gdx.graphics.getWidth()*.25f);


        lamp.setSize(Gdx.graphics.getWidth()*.5f,Gdx.graphics.getWidth()*.46f);
        lamp.setPosition(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f, Align.center);
        lamp.addAction(sequence(fadeIn(1f),moveToAligned(stage.getWidth()/2f,stage.getHeight()/2f,Align.center|Align.bottom,1,Interpolation.swingOut),run(new Runnable() {
            @Override
            public void run() {
                table.addActorBefore(lamp,logo);
            }
        })));



        logo.setOrigin(Align.center);
        logo.setPosition(stage.getWidth()/2f,stage.getHeight(),Align.center);
        logo.addAction(sequence(scaleTo(.1f,.1f),
                parallel(fadeIn(2f,Interpolation.pow2),scaleTo(1,1,2.5f,Interpolation.pow5),sequence(delay(1.5f),run(new Runnable() {
                            @Override
                            public void run() {
                                game.audioManager.slide();
                            }
                        })),
                moveToAligned(stage.getWidth()/2f, stage.getHeight()/2-20*coefficientWidth, Align.center|Align.top, 2f,Interpolation.swing))
        ,delay(.3f),run(new Runnable() {
                    @Override
                    public void run() {
                        back();
                    }
                })));


        table.addActor(libgdx);

        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        stage.act();
        stage.draw();
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
        texture.dispose();
        texture2.dispose();
        texture3.dispose();
        texture4.dispose();
        stage.dispose();
    }


    @Override
    public void slideStart() {

    }

    @Override
    public void slideBack() {

    }

    @Override
    public void back() {
        game.remove();
        game.add(mainMenu);
        game.audioManager.music.play();
    }
}
