package com.bulbels.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.bulbels.game.Bulbels;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveToAligned;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class InfoScreen extends MyScreen {
    Label titers, company;
    InfoScreen(Bulbels game) {
        super(game);
        table.setFillParent(true);

        titers = new Label("Разработчик, дизайнер \n" +
                "и автор игры: \n Иван Донской \n\n" +
                "Описание:\n" +
                "Игра создана по мотивам игры Ballz,\n" +
                "Но в отличии от неё, в моей игре есть\n" +
                "Разнообразные бонусы, интересные скины,\n" +
                "Различные ировые фигуры и капелька \n" +
                "Вдохновения.  \n" +
                "Надеюсь, вам понравится!\n" +
                "Приятного времяпровождения!\n\n" +
                "Музыка\n" +
                "The Arcadium, Zukira - Outside World\n\n" +
                " Создано при поддержке \n" +
                "  IT school Samsung "
                ,new Skin(Gdx.files.internal("skins/label_skin.json"),game.atlas),"white");
        titers.setAlignment(Align.center|Align.top);
        titers.setFontScale(Bulbels.coefficientWidth*.5f);
        titers.pack();

        company = new Label( "© V idea \n Все права не защищены.\n",titers.getStyle());
        company.setFontScale(titers.getFontScaleX(),titers.getFontScaleY());
        company.setAlignment(Align.center|Align.top);
        company.pack();

        table.addActor(company);
        table.addActor(titers);
        table.setX(-Gdx.graphics.getWidth());
        stage.addActor(table);
//        stage.setDebugAll(true);
    }


    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer(stage, this);
        Gdx.input.setInputProcessor(multiplexer);
        titers.clearActions();
        titers.setPosition(Gdx.graphics.getWidth()/2f,0,Align.center|Align.top);
        titers.addAction(sequence(delay(1),moveBy(0,Gdx.graphics.getHeight()+titers.getHeight(), 20f)));
        company.clearActions();
        company.setPosition(Gdx.graphics.getWidth()/2f,0,Align.center|Align.top);
        company.addAction(sequence(delay(18),moveToAligned(stage.getWidth()/2,stage.getHeight()/2,Align.center,2,Interpolation.swing)));

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

    }

    @Override
    void slideStart() {
        table.addAction( sequence(moveBy(Gdx.graphics.getWidth(), 0, .6f, Interpolation.swingOut)));
    }

    @Override
    void slideBack() {
        table.addAction( sequence(moveBy(-Gdx.graphics.getWidth(), 0, .6f, Interpolation.swingIn), run(back)));
    }

    @Override
    public void back() {
        slideBack();
    }
}
