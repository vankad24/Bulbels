package com.bulbels.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.bulbels.game.Bulbels;
import com.bulbels.game.models.MyActor;
import com.bulbels.game.models.balls.Skin;
import com.bulbels.game.models.boosts.Boost;
import com.bulbels.game.models.boosts.AimBoost;
import com.bulbels.game.models.boosts.DamageBoost;
import com.bulbels.game.models.boosts.GhostBoost;
import com.bulbels.game.models.boosts.SizeBoost;
import com.bulbels.game.models.boosts.SmallBoost;
import com.bulbels.game.models.boosts.SpeedBoost;
import com.bulbels.game.utils.BoostData;
import com.bulbels.game.utils.MySprite;
import com.bulbels.game.utils.ShopData;

import java.util.Random;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.bulbels.game.Bulbels.coefficientHeight;
import static com.bulbels.game.Bulbels.coefficientWidth;

public class ShopScreen extends MyScreen {
    boolean skins=true;
    ScrollPane skinsPane, boostsPane;
    Table scrollTable1, scrollTable2;
    public static Array<Panel> panels;
    public static Array<BoostPanel> boostPanels;
    float width, height;
    public int indexOfSelected;
    MyActor coins, topBar;
    ColorButton skinsButton, boostButton;
    Label labelMoney;
    com.badlogic.gdx.scenes.scene2d.ui.Skin labelSkin;
    public boolean[] bought;
    public int[] level, amount;
    Color[] colors;
    static Random rand =new Random();
    public ShopScreen(Bulbels game) {
        super(game);
        labelSkin =  new com.badlogic.gdx.scenes.scene2d.ui.Skin(Gdx.files.internal("skins/label_skin.json"));
        scrollTable1 = new Table();
        float topY = Gdx.graphics.getHeight()*.9f;
        topBar = new MyActor(game.atlas.findRegion("square"),0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight()-topY);

        height = (topY-80*coefficientHeight)/2;
        width = height/1.25f;
        /*scrollTable.add(startButton).minWidth(130);
        scrollTable.add(settingsButton).minWidth(130).row();*/
        colors =new Color[]{Color.GREEN,Color.YELLOW, Color.RED};
        String[] types = {"common_item","common_item","common_item","common_item","common_item","gold_item","gold_item","gold_item","gold_item","rainbow_item",
                          "common_item","common_item","common_item","common_item","common_item","gold_item","gold_item","gold_item","gold_item","rainbow_item"};
        int[] prices = {0,  100,100,100,100,200,200,200,300,400,
                        100,100,100,100,100,200,200,200,300,400};
        int[] skinsId = {Skin.ColorSkin.WHITE, Skin.ColorSkin.CYAN, Skin.ColorSkin.GREEN, Skin.ColorSkin.GOLD, Skin.ColorSkin.ORANGE,Skin.TextureSkin.TENNIS,Skin.TextureSkin.BASKETBALL,Skin.TextureSkin.BASEBALL,Skin.TextureSkin.EARTH,Skin.TextureSkin.RAINBOW_FOOTBALL,
                        Skin.ColorSkin.RED, Skin.ColorSkin.BLUE, Skin.ColorSkin.YELLOW, Skin.ColorSkin.LIME, Skin.ColorSkin.PINK, Skin.TextureSkin.BOWLING,Skin.TextureSkin.BILLIARD,Skin.TextureSkin.FOOTBALL,Skin.TextureSkin.COLORFUL,Skin.TextureSkin.ENDER_PERL};

        String[] types2 = {"common_item","common_item","common_item","gold_item","gold_item","rainbow_item"};
        int[] prices2 = {100,100,100,200,200,400};
        int[][] upgradePrices = {new int[]{50,100,150},new int[]{100,150,200},new int[]{100,150,200},new int[]{150,200,300},new int[]{200,400},new int[]{200,300,500}};
        Boost[] arrayBoosts = { new SpeedBoost(), new SizeBoost(),new SmallBoost(), new AimBoost(),new DamageBoost(), new GhostBoost()};


        panels = new Array<>();
        game.settings.loadShop(this, new int[]{types.length,types2.length});
        bought[0]=true;
        for (int i = 0; i < types.length; i++) {
            Panel panel = new Panel(types[i], skinsId[i], prices[i], bought[i]);
            panels.add(panel);
            scrollTable1.add(panel).pad(20*coefficientHeight);
            if (i+1 == types.length/2)scrollTable1.row();
        }


        panels.get(indexOfSelected).select();




        //scrollTable.add(group).width(group.getWidth()).height(group.getHeight());

      //  ButtonGroup buttonGroup = new ButtonGroup();
       // buttonGroup.add(startButton,settingsButton);

//        scrollTable.add(actor).pad(40).padLeft(1000);

        skinsPane = new ScrollPane(scrollTable1,new com.badlogic.gdx.scenes.scene2d.ui.Skin(Gdx.files.internal("skins/scroll_pane_skin.json"),game.atlas));
        skinsPane.setScrollingDisabled(false,true);
        //scrollPane.setBounds(0, 0, stage.getWidth(), stage.getHeight());

        scrollTable2 = new Table();
        boostPanels = new Array<>();



        for (int i = 0; i < types2.length; i++) {
            BoostPanel panel =new BoostPanel(types2[i],arrayBoosts[i] , prices2[i],upgradePrices[i], amount[i], level[i]);
            boostPanels.add(panel);
            scrollTable2.add(panel).pad(20*coefficientHeight);
        }
        scrollTable2.row();
        for (int i = 0; i < types2.length; i++) {
            scrollTable2.add(boostPanels.get(i).upgrade).pad(20*coefficientHeight);
        }

        boostsPane = new ScrollPane(scrollTable2, new com.badlogic.gdx.scenes.scene2d.ui.Skin(Gdx.files.internal("skins/scroll_pane_skin.json"),game.atlas));
        boostsPane.setScrollingDisabled(false,true);
        skinsButton = new ColorButton(topBar.getHeight(),topBar.getHeight(),"skins");
        skinsButton.setPosition(100*coefficientWidth,topY);
        skinsButton.button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!skins) {
                    changeShop();
                    boostsPane.setVisible(false);
                    skinsPane.setVisible(true);
                }
            }
        });

        boostButton = new ColorButton(skinsButton.getWidth(),skinsButton.getHeight(),"boosts");
        boostButton.setPosition(skinsButton.getRight()+20*coefficientWidth, skinsButton.getY());
        boostButton.button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (skins) {
                    changeShop();
                    table.swapActor(skinsPane,boostsPane);
//                    skinsPane.remove();
                    skinsPane.setVisible(false);
                    boostsPane.setVisible(true);
                }
            }
        });
        boostButton.dark();

        Label.LabelStyle style =new Label.LabelStyle(game.font,Color.YELLOW);

        labelMoney = new Label("555",style);
        labelMoney.setFontScale(topBar.getHeight()/112);
        labelMoney.pack();
        labelMoney.setPosition(topBar.getRight()-10*coefficientWidth,+topBar.getHeight()/2,Align.center|Align.right);
        coins = new MyActor(game.atlas.findRegion("coin"),0,0,topBar.getHeight()*.6f,topBar.getHeight()*.6f);
        coins.setPosition(labelMoney.getX()-5*coefficientWidth,topBar.getHeight()/2,Align.center|Align.right);
        //skinsButton.setPosition(50,topY);

        Group group = new Group();
        group.setSize(topBar.getWidth(),topBar.getHeight());
        group.addActor(topBar);
        group.addActor(coins);
        group.addActor(labelMoney);

        //scrollTable.setPosition(0,topY-20*coefficientWidth,Align.topLeft);
        table.add(group).row();
//        skinsPane.setPosition(0,topY,Align.topLeft);
       // table.add(skinsPane).height(topY).row();
//        boostsPane.setBounds(skinsPane.getX(),skinsPane.getY(),skinsPane.getWidth(),skinsPane.getHeight());
//        table.addActor(boostsPane);
//        table.add(boostsPane).height(topY).width(Gdx.graphics.getWidth());

        table.addActor(skinsPane);
        skinsPane.setSize(Gdx.graphics.getWidth(),topY);

        table.addActor(boostsPane);
        boostsPane.setSize(Gdx.graphics.getWidth(),topY);
        boostsPane.setVisible(false);

        table.addActor(skinsButton);
        table.addActor(boostButton);

        table.setFillParent(true);
        table.align(Align.center|Align.top);
        table.setY(-Gdx.graphics.getHeight());
        stage.addActor(table);

//         stage.setDebugAll(true);
    }

    public static void addBoost(){
        ShopScreen.BoostPanel panel =boostPanels.get(rand.nextInt(boostPanels.size));
        panel.amount++;
        panel.updateLabel();
    }

    public static void addSkin(){
        ShopScreen.Panel panel = panels.get(rand.nextInt(panels.size));
        while (panel.bought)panel = panels.get(rand.nextInt(panels.size));
        panel.buy();
        panel.deselect();
        panel.light();
    }

    TextButton createButton(float width, float height, String text, Color color, String nameUp, String nameDown){
        com.badlogic.gdx.scenes.scene2d.ui.Skin tempSkin = new com.badlogic.gdx.scenes.scene2d.ui.Skin();
        tempSkin.load(Gdx.files.internal("skins/button_skin.json"));
        TextButton button = new TextButton(text, tempSkin);
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


    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer(stage, this);
        Gdx.input.setInputProcessor(multiplexer);
        updateLabels();
        updatePanels();
        updateColors();
    }

    @Override
    public void render(float delta) {

        stage.act();
        stage.draw();
//        System.out.println(actor.sprite.getX()+" "+actor.isVisible()+" "+actor.ancestorsVisible()+" "+actor.hasKeyboardFocus()+" "+actor.hasScrollFocus()+" "+actor.hasParent());
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
        table.addAction( sequence(moveBy(0, Gdx.graphics.getHeight(), .6f, Interpolation.swingOut)));
    }

    @Override
    public void slideBack() {
        table.addAction( sequence(moveBy(0, -Gdx.graphics.getHeight(), .6f, Interpolation.swingIn), run(back)));
    }

    @Override
    public void back() {
        slideBack();
        game.skin.setSkinId(panels.get(indexOfSelected).skin);
        game.settings.saveShop(this);
    }

    void changeShop(){
        if (skins){
            skinsButton.dark();
            boostButton.light();
        }else {
            boostButton.dark();
            skinsButton.light();
        }
        skins=!skins;
    }
    void updateColors(){
        skinsButton.updateColors();
        boostButton.updateColors();
        if (skins){
            skinsButton.light();
            boostButton.dark();
        }else {
            boostButton.light();
            skinsButton.dark();
        }
        topBar.setColor(game.second_backgroundColor);

    }

    void updateLabels(){
        labelMoney.setText(game.money);
    }

    void updatePanels(){
        for (Panel p: panels)if (!p.bought)p.update();
        for (BoostPanel p: boostPanels){
            p.update();
            p.upgrade.update();
        }
    }

    public ShopData[] getPanelsData(){
        ShopData[] shopData = new ShopData[panels.size];
        for (int i = 0; i < panels.size; i++) shopData[i] = panels.get(i).getData();
        return shopData;
    }

    public BoostData[] getBoostData(){
        BoostData[] boostData = new BoostData[boostPanels.size];
        for (int i = 0; i < boostPanels.size; i++) boostData[i] = boostPanels.get(i).getBoostData();
        return boostData;
    }
    class Panel extends Group {
        boolean bought;
        int price, skin;
        Image ball;
        Label label;
        Image mark;
        public Image button;
        float coeff;

        public Panel(String typeName, int skin, int price, boolean bought) {
            this.skin = skin;
            this.bought =bought;
            this.price = price;
            coeff = width / 300;
            setSize(width, height);
            button = new Image(game.atlas.findRegion(typeName));
            button.setSize(width, height);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("clickeeed");
                    select();
                    if (Panel.this.bought)game.audioManager.click();
                    else {
                        game.audioManager.buy.play(game.audioManager.getVolumeEffects());
                        game.money -= Panel.this.price;
                        buy();
                        updatePanels();
                        updateLabels();
                    }
                }
            });
            addActor(button);
            ball = new Image(new SpriteDrawable(game.skin.getSprite(skin)));
            ball.setOrigin(Align.center);
            ball.setPosition(width / 2, height * .7f, Align.center);
            ball.setScale(coeff);
            ball.setTouchable(Touchable.disabled);
//        MyActor actor = new MyActor(sprite,width*.25f,group.getHeight()*.6f,width/2,width/2);
//        actor.setTouchable(Touchable.disabled);
            addActor(ball);

            label = new Label(price + "",labelSkin, "white");
            // System.out.println(label.getPrefWidth());
            label.setFontScale(.5f * coeff);
            label.pack();
            //System.out.println(label.getPrefWidth());

            label.setPosition(width / 2, height * .2f, Align.center);
            label.setTouchable(Touchable.disabled);
            if (bought) {
                mark = new Image(new Sprite(game.atlas.findRegion("checkmark")));
                deselect();
            }else mark = new Image(new Sprite(game.atlas.findRegion("coin")));
            mark.setBounds(label.getRight() + 10 * coefficientWidth, label.getY(), label.getPrefHeight(), label.getPrefHeight());
            mark.setTouchable(Touchable.disabled);
            addActor(mark);

            addActor(label);

//            scrollTable1.add(this).pad(20*coefficientHeight);
//        System.out.println(group.getWidth()+" "+group.getHeight()+" "+button.getWidth()+" "+button.getHeight());

//        return group;
        }


        void buy(){
            Panel.this.bought = true;
            mark.setDrawable(new SpriteDrawable(new Sprite(game.atlas.findRegion("checkmark"))));
        }
        void updateLabel(){
            label.pack();
            label.setPosition(width / 2, height * .2f, Align.center);
            mark.setBounds(label.getRight() + 10 * coefficientWidth, label.getY(), label.getPrefHeight(), label.getPrefHeight());
        }

        void dark(){
            label.setColor(Color.DARK_GRAY);
            mark.setColor(Color.DARK_GRAY);
            ball.setColor(Color.DARK_GRAY);
            button.setColor(Color.DARK_GRAY);
            button.setTouchable(Touchable.disabled);
        }
        void light(){
            label.setColor(Color.WHITE);
            mark.setColor(Color.WHITE);
            ball.setColor(Color.WHITE);
            button.setColor(Color.WHITE);
            button.setTouchable(Touchable.enabled);
        }

        void update(){
            if (game.money<price)dark();
            else light();
        }

        void select(){
            panels.get(indexOfSelected).deselect();
            indexOfSelected = panels.indexOf(this,true);
            mark.setVisible(true);
            label.setText(game.androidHelper.getString("chosen"));
            updateLabel();
        }
        void deselect(){
            mark.setVisible(false);
            label.setText(game.androidHelper.getString("bought"));
            updateLabel();
        }

        public ShopData getData(){ return new ShopData(bought);}

    }

    class BoostPanel extends Panel {
        Label labelAmount;
        int level,amount;
        int[] upgradePrices;
        Boost boost;
        String type;
        Panel upgrade;
        Image[] images;

        public BoostPanel(String typeName, Boost boost, int price, final int upgradePrices[], final int amount, final int level) {
            super(typeName, 0, price, false);
            this.amount = amount;
            this.level = level;
            this.boost = boost;
            type = typeName;
            this.upgradePrices=upgradePrices;
            label.moveBy(0, height * .1f);
            mark.moveBy(0, height * .1f);
            labelAmount = new Label("x100", labelSkin, "white");
            labelAmount.setFontScale(coeff * .5f);
            labelAmount.pack();
            labelAmount.setPosition(width / 2, label.getY() - 10 * coefficientHeight, Align.center | Align.top);
            labelAmount.setTouchable(Touchable.disabled);
            updateLabel();
            addActor(labelAmount);
            ball.setDrawable(new TextureRegionDrawable(game.atlas.findRegion(boost.getRegionName())));

            button.clearListeners();
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.audioManager.buy.play(game.audioManager.getVolumeEffects());
                    game.money -= BoostPanel.this.price;
                    BoostPanel.this.amount++;
                    updateLabel();
                    updateLabels();
                    updatePanels();
                }
            });




            final Image back_color = new Image(new SpriteDrawable(new MySprite(game.atlas.findRegion("square")).color(Color.GOLD)));
            back_color.setSize(width*.7f, height*.2f);
            back_color.setPosition(width/2,ball.getY()*.3f,Align.center);
            back_color.setOrigin(Align.center);
            back_color.setTouchable(Touchable.disabled);

            final Image color = new Image(new SpriteDrawable(new MySprite(game.atlas.findRegion("square")).color(Color.LIGHT_GRAY)));
            color.setSize(back_color.getWidth()*.95f,back_color.getHeight()*.85f);
            color.setPosition(width/2,back_color.getY()+back_color.getOriginY(),Align.center);
            color.setTouchable(Touchable.disabled);

            final Group group = new Group();
            group.setSize(color.getWidth(),color.getHeight());
            group.setPosition(color.getX(),color.getY());
            float width = group.getWidth()/upgradePrices.length;
            images = new Image[upgradePrices.length];
            for (int i = 0; i < upgradePrices.length; i++) {
                Image image =new Image(new SpriteDrawable(new MySprite(game.atlas.findRegion("color_pane")).color(colors[upgradePrices.length-1-i])));
                image.setSize(width,group.getHeight());
                image.setPosition(i*width,0);
                group.addActor(image);
                images[i]=image;
            }


            upgrade = new Panel(typeName,0,100 , false){
                @Override
                void dark() {
                    super.dark();
                    for (Actor a:images)a.setColor(Color.DARK_GRAY);
                    back_color.setColor(Color.DARK_GRAY);
                    color.setColor(Color.DARK_GRAY);
                }

                @Override
                void light() {
                    super.light();
                    for (Actor a:images)a.setColor(Color.WHITE);
                    back_color.setColor(Color.WHITE);
                    color.setColor(Color.WHITE);
                }
                @Override
                void select() {}
                @Override
                void deselect() {}

                @Override
                void updateLabel() {
                    label.setText(price);
                }
            };
            upgrade.ball.setDrawable(new TextureRegionDrawable(game.atlas.findRegion("boosts")));
            upgrade.label.moveBy(0, ball.getY()*.3f);
            upgrade.mark.moveBy(0, ball.getY()*.3f);
            upgrade.button.clearListeners();
            upgrade.button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.audioManager.buy.play(game.audioManager.getVolumeEffects());
                    game.money -= upgrade.price;
                    BoostPanel.this.level++;
                    updateLevel();
                    upgrade.updateLabel();
                    updateLabels();
                    updatePanels();
                }
            });
            upgrade.addActor(back_color);
            upgrade.addActor(color);
            upgrade.addActor(group);
            updateLevel();
            upgrade.updateLabel();


        }


        @Override
        void dark() {
            super.dark();
            labelAmount.setColor(Color.DARK_GRAY);
        }

        @Override
        void light() {
            super.light();
            labelAmount.setColor(Color.WHITE);
        }

        @Override
        void select() {
        }

        @Override
        void updateLabel() {
            labelAmount.setText("x" + amount);
        }

        @Override
        void deselect() {
        }

        void updateLevel(){
            for (int i = 0; i < level; i++)images[i].setVisible(true);
            for (int i = level; i < images.length; i++)images[i].setVisible(false);
            if (level!=upgradePrices.length)upgrade.price = upgradePrices[level];
            else {
                upgrade.setTouchable(Touchable.disabled);
                upgrade.label.setVisible(false);
                upgrade.mark.setVisible(false);
                upgrade.price=0;
            }
        }

        public BoostData getBoostData(){return new BoostData(level,amount);}
    }

    class ColorButton extends Group{
        public Image back_color, button, image;
        Color b_color, c_color;
        public ColorButton(float width, float height, String textureName) {
            setSize(width, height);
            back_color = new Image(game.atlas.findRegion("square"));
//            back_color = new Image(new SpriteDrawable(new MySprite(game.atlas.findRegion("square")).color(game.backgroundColor)));
            back_color.setSize(width*.9f, height*.95f);
            back_color.setTouchable(Touchable.disabled);
            back_color.setPosition(width/2,0,Align.center|Align.bottom);


            button = new Image(game.atlas.findRegion("square"));
//            button = new Image(new MySprite(game.atlas.findRegion("square")).color(new Color(game.backgroundColor).mul(game.dark_backgroundColor))));
            button.setSize(width, height);
            button.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.audioManager.click();
                }
            });

            image = new Image(new MySprite(game.atlas.findRegion(textureName)));
            image.setSize(back_color.getWidth()*.8f,back_color.getHeight()*.8f);
            image.setPosition(width/2,back_color.getHeight()/2,Align.center);
            image.setTouchable(Touchable.disabled);
            addActor(button);
            addActor(back_color);
            addActor(image);
            updateColors();
        }

        void dark() {
            back_color.setColor(new Color(c_color).mul(Color.GRAY));
            button.setColor(new Color(b_color).mul(Color.GRAY));
            image.setColor(Color.GRAY);
        }

        void light() {
            back_color.setColor(c_color);
            button.setColor(b_color);
            image.setColor(Color.WHITE);
        }
        void updateColors(){
            b_color =new Color(game.backgroundColor).mul(game.second_backgroundColor);
            c_color = game.backgroundColor;
            back_color.setColor(c_color);
            button.setColor(b_color);
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (pointer==1){
            game.money+=100;
            updateLabels();
            updatePanels();
        }
        return true;

    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.M:
                game.money+=100;
                updateLabels();
                updatePanels();
                break;
            case Input.Keys.Z:
                game.money=0;
                updateLabels();
                updatePanels();
                break;
            case Input.Keys.B:
                System.out.println(skinsButton.getX()+" "+skinsButton.getY()+" "+skinsButton.getWidth()+" "+skinsButton.getHeight());
                break;
        }
        return super.keyDown(keycode);
    }
}
