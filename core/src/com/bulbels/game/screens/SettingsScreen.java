package com.bulbels.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.bulbels.game.Bulbels;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.bulbels.game.Bulbels.coefficientHeight;
import static com.bulbels.game.Bulbels.coefficientWidth;

public class SettingsScreen extends MyScreen {

    Setting volumeEffects, volumeMusic, generalVolume;
    Array<ColorPane> panes;

    SettingsScreen(Bulbels game) {
        super(game);

        //slider.setSize(Gdx.graphics.getWidth(), 50);

//        table.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight(),Align.center|Align.top);

        float size =450*coefficientWidth;
        float width = Gdx.graphics.getWidth()/5f;
        generalVolume = new Setting(game.androidHelper.getString("general_volume"), size,size*.15f );
        table.row().align(Align.center);
        volumeEffects = new Setting(game.androidHelper.getString("effect_volume"), size,size*.15f );
        table.row();
        volumeMusic = new Setting(game.androidHelper.getString("music_volume"), size,size*.15f );
        table.row();


        panes = new Array<>();
        panes.add(new ColorPane(new Color(.17f,.17f,.17f,1), new float[]{.05f,.05f,.05f,1}, width),
                new ColorPane(new Color(0,0,.4f,1),new float[]{.0f,.0f,.2f,1},width),
                new ColorPane(new Color(.34f,.34f,.34f,1),new float[]{-.08f,-.08f,-.08f,1},width),
                new ColorPane(Color.ROYAL,new float[]{-.08f,-.08f,-.08f,1},width));
        table.setFillParent(true);
        table.setX(Gdx.graphics.getWidth());
        stage.addActor(table);
    }

    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer(stage, this);
        Gdx.input.setInputProcessor(multiplexer);
        generalVolume.setValue(game.audioManager.volume);
        volumeEffects.setValue(game.audioManager.volumeEffects);
        volumeMusic.setValue(game.audioManager.volumeMusic);
        panes.get(game.indexOfColor).select();


    }

    void updateVolume(){
        game.audioManager.volumeEffects = volumeEffects.getValue();
        game.audioManager.volume = generalVolume.getValue();
        game.audioManager.volumeMusic = volumeMusic.getValue();
        game.audioManager.music.setVolume(game.audioManager.getVolumeMusic());

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
    public void slideStart() {
        table.addAction( sequence(moveBy(-Gdx.graphics.getWidth(), 0, .6f, Interpolation.swingOut)));
    }

    @Override
    public void slideBack() {
        table.addAction( sequence(moveBy(Gdx.graphics.getWidth(), 0, .6f, Interpolation.swingIn), run(back)));
    }

    @Override
    public void back() {
        slideBack();
        updateVolume();
    }

    class Setting extends Group {

        final Slider slider;
        final Label percent;
        final Image mute;

        public Setting(final String name, final float width, final float height) {
            super();

            percent = new Label("100%", new Skin(Gdx.files.internal("skins/label_skin.json")), "white");
            percent.setFontScale(height / 112);
            percent.pack();

            percent.setPosition(width + 10 * coefficientWidth, height / 2, Align.center | Align.left);

            mute = new Image(game.atlas.findRegion("mute"));
            mute.setSize(height * .8f, height * .8f);
            mute.setPosition(percent.getX(), height / 2, Align.center | Align.left);
            mute.setVisible(false);

            Sprite background = new Sprite(game.atlas.findRegion("slider_background"));
            background.setSize(width, height * .4f);
            Sprite knob = new Sprite(game.atlas.findRegion("slider_knob"));
            knob.setSize(height, height);
            Slider.SliderStyle style = new Slider.SliderStyle(new SpriteDrawable(background), new SpriteDrawable(knob));
            slider = new Slider(0, 1, .01f, false, style);
            slider.setSize(width, height);
            slider.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    this.touchDragged(event, x, y, pointer);
                    game.audioManager.click();
                    return true;
                }

                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    setValue(slider.getPercent());
                    updateVolume();
                    super.touchDragged(event, x, y, pointer);
                }
            });

            final Label label = new Label(name, new Skin(Gdx.files.internal("skins/label_skin.json")), "white");
            label.setFontScale(height / 112);
            label.pack();

            label.setPosition(width / 2, height + 10 * coefficientHeight, Align.center | Align.bottom);
            addActor(slider);
            addActor(label);
            addActor(percent);
            addActor(mute);
            setSize(percent.getRight(), label.getTop());
            table.add(this).padBottom(40*coefficientHeight).colspan(4);
        }
        float getValue(){return slider.getValue(); }

        void setValue(float value){
            slider.setValue(value);
            if (value == 0) {
                percent.setText("");
                mute.setVisible(true);
            } else {
                percent.setText((int) (value * 100) + "%");
                mute.setVisible(false);
            }
        }
    }

    class ColorPane extends Group{
        Color color;
        float[] color2;
        Image mark;

        public ColorPane(final Color color, final float[] color2, float width) {
            super();
            setSize(width,width);

            this.color = color;
            this.color2 = color2;
            Sprite sprite1 = new Sprite(game.atlas.findRegion("color_pane"));
            sprite1.setColor(Color.DARK_GRAY);
            sprite1.setSize(width, width);
            ImageButton background = new ImageButton(new SpriteDrawable(sprite1));
            background.setSize(width, width);
            background.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    setColor();
                    game.audioManager.click();
                    select();
                }
            });

            Sprite sprite2 = new Sprite(game.atlas.findRegion("color_pane"));
            sprite2.setColor(color);
            Image imageColor = new Image(new SpriteDrawable(sprite2));
            imageColor.setSize(width*.95f,width*.95f);
            imageColor.setPosition(width/2,width/2, Align.center);
            imageColor.setTouchable(Touchable.disabled);

            mark = new Image(new Sprite(game.atlas.findRegion("checkmark")));
            mark.setSize(width*.2f,width*.2f);
            mark.setPosition(width, 0, Align.bottomRight);
            mark.setTouchable(Touchable.disabled);
            deselect();

            addActor(background);
            addActor(imageColor);
            addActor(mark);
            table.add(this).pad(10*coefficientWidth);
        }
        void setColor(){
            game.backgroundColor = color;
            game.second_backgroundColor = new Color(color).add(color2[0],color2[1],color2[2],color2[3]);
        }
        void deselect() { mark.setVisible(false);}
        void select() {
            panes.get(game.indexOfColor).deselect();
            game.indexOfColor = panes.indexOf(this,true);
            mark.setVisible(true);
        }
    }

}
