package com.bulbels.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.bulbels.game.Bulbels;

import static com.bulbels.game.Bulbels.coefficientHeight;
import static com.bulbels.game.Bulbels.coefficientWidth;

public class ButtonPanel extends Group {
    Image background, imageColor;
    public float bottom;

    @Override
    protected void sizeChanged() {
        background.setSize(getWidth(),getHeight());
        imageColor.setSize(getWidth()*.98f,getHeight()*.98f);

    }
    public ButtonPanel(Bulbels game, String title, float width, String[] texts, Color[] colors, InputListener[] listeners) {this(game,title,width,texts,colors,listeners,null);}
    public ButtonPanel(final Bulbels game, String title, float width, String[] texts, Color[] colors, InputListener[] listeners, Actor[] actors) {
        super();

        float buttWidth = width*.9f;
        float buttHeight = buttWidth*.17f;

        Label label = new Label(title,new Skin(Gdx.files.internal("skins/label_skin.json")),"yellow");
        label.setFontScale(coefficientHeight);
        label.pack();

        float height =Math.max(width,label.getHeight()+10*coefficientHeight+buttHeight*1.5f*texts.length);
        if (actors!=null){
            for (int i = 0; i <actors.length ; i++) {
                addActor(actors[i]);
                actors[i].setPosition(width/2,10*(i+1)+actors[i].getHeight(),Align.center|Align.top);
                height+=actors[i].getHeight()+10;
            }
        }
        background = new Image(new SpriteDrawable(new MySprite(game.atlas.findRegion("color_pane")).size(width, width).color(Color.DARK_GRAY)));
        background.setSize(width,height);
        background.setPosition(width/2,width/2, Align.center);
        background.setTouchable(Touchable.disabled);


        imageColor = new Image(new SpriteDrawable(new MySprite(game.atlas.findRegion("color_pane")).size(width*.98f,height*.98f).color(Color.GRAY)));
        imageColor.setSize(width*.98f,height*.98f);
        imageColor.setPosition(width/2,width/2, Align.center);
        imageColor.setTouchable(Touchable.disabled);

        label.setPosition(width/2, imageColor.getTop(),Align.center|Align.top);

        addActor(background);
        addActor(imageColor);
        addActor(label);
//        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle()
        Skin skin = new Skin(Gdx.files.internal("skins/button_skin.json"),game.atlas);
        for (int i = 0; i < texts.length; i++) {
            TextButton textButton = new TextButton(texts[i],skin,"default");
            textButton.setColor(colors[i]);
            textButton.setSize(buttWidth, buttHeight);
            textButton.getLabel().setFontScale(coefficientWidth);
            textButton.addListener(listeners[i]);
            textButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.audioManager.click();
                }
            });
            textButton.setPosition(width/2,label.getY()-10*coefficientHeight-buttHeight*1.5f*i, Align.center|Align.top);
            addActor(textButton);
            bottom = textButton.getY();
        }
        setSize(width,height);
    }
}
