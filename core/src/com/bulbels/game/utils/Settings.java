package com.bulbels.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.bulbels.game.Bulbels;
import com.bulbels.game.models.balls.AllBalls;
import com.bulbels.game.models.balls.Skin;
import com.bulbels.game.screens.GameField;
import com.bulbels.game.screens.ShopScreen;
import com.google.gson.Gson;


public class Settings {
    class FieldValues{
        ShapeData[][] shapes;
        int turn, amountOfBalls;
        float startX, startY;
    }
    class  GameValues{
        int money, skinId, indexOfColor, record;
        float volumeEffects, volumeMusic, volume;
    }
    class ShopValues{
        ShopData[] shopData;
        BoostData[] boostData;
        int indexOfSelected;
    }


        Bulbels game;
    Preferences preferences;
    Gson gson = new Gson();
    FieldValues fieldValues;
    GameValues gameValues;
    ShopValues shopValues;
    public Settings(Bulbels game) {
        this.game = game;
        preferences = Gdx.app.getPreferences("settings");
//        clear();
    }

    public void saveShop(ShopScreen shop){
        shopValues = new ShopValues();
        shopValues.shopData = shop.getPanelsData();
        shopValues.boostData = shop.getBoostData();
        shopValues.indexOfSelected = shop.indexOfSelected;
        preferences.putString("shop", gson.toJson(shopValues));
    }

    public void loadShop(ShopScreen shop, int[] len){
        boolean[] bought = new boolean[len[0]];
        int[] level = new int[len[1]], amount = new int[len[1]];
        if (preferences.contains("shop")) {
            shopValues = gson.fromJson(preferences.getString("shop"), ShopValues.class);
            shop.indexOfSelected =shopValues.indexOfSelected;
            for (int i = 1; i < len[0]; i++) {
                bought[i]=shopValues.shopData[i].bought;
            }
            for (int i = 0; i < len[1]; i++) {
                level[i] = shopValues.boostData[i].level;
                amount[i] = shopValues.boostData[i].amount;
            }
            System.out.println("shop loaded");

        }else {
            shop.indexOfSelected = 0;

            System.out.println("poop");

        }
        shop.bought = bought;
        shop.level = level;
        shop.amount = amount;
    }

    public void saveGame(Bulbels game){
        gameValues = new GameValues();
        gameValues.money = game.money;
        gameValues.skinId = game.skin.getSkinId();
        gameValues.volume = game.audioManager.volume;
        gameValues.volumeEffects = game.audioManager.volumeEffects;
        gameValues.volumeMusic = game.audioManager.volumeMusic;
        gameValues.indexOfColor = game.indexOfColor;
        gameValues.record = game.record;
        String s = gson.toJson(gameValues);
        System.out.println(":"+s);

        preferences.putString("game", s);

    }
    public void loadGame(Bulbels game){
        if (preferences.contains("game")) {
            gameValues = gson.fromJson(preferences.getString("game"), GameValues.class);
            game.money = gameValues.money;
            game.skin.setSkinId(gameValues.skinId);
            game.audioManager.volume = gameValues.volume;
            game.audioManager.volumeEffects = gameValues.volumeEffects;
            game.audioManager.volumeMusic = gameValues.volumeMusic;
            game.indexOfColor = gameValues.indexOfColor;
            game.record = gameValues.record;
            System.out.println("game loaded");

        }else {
            System.out.println("poop");
            game.money =0;
            game.skin.setSkinId(Skin.ColorSkin.WHITE);
            game.audioManager.volume = 1;
            game.audioManager.volumeEffects =1;
            game.audioManager.volumeMusic = 1;
            game.indexOfColor =0;
            game.record =0;
        }
    }

    public void saveField(GameField field){
        System.out.println("save");
        try {
            fieldValues = new FieldValues();
            if (!field.finish){
                fieldValues.amountOfBalls++;
                field.allShapes.generateNewLine();
                GameField.turn++;
            }
            fieldValues.shapes = field.allShapes.getArrayData();
            fieldValues.turn=GameField.turn;
            fieldValues.amountOfBalls =field.amountOfBalls;
            fieldValues.startX = AllBalls.startX;
            String s = gson.toJson(fieldValues);
            System.out.println(":"+s);

            preferences.putString("field", s);
        }catch (Exception e){
            System.out.println("poop");
        }
        //Shape square = gson.fromJson(preferences.getString("shapes"), Values.class).shape;
//        System.out.println("square "+square.duration);



    }
    public void loadField(GameField field){
        if (preferences.contains("field")){
            fieldValues = gson.fromJson(preferences.getString("field"),FieldValues.class);
            field.allShapes.loadData(fieldValues.shapes);
            AllBalls.startX = fieldValues.startX;
            GameField.turn = fieldValues.turn;
            field.allBalls.addBall(fieldValues.amountOfBalls);
            field.amountOfBalls = fieldValues.amountOfBalls;
            field.updateLabels();
            System.out.println("field loaded");
        }else {
            field.allBalls.addBall(AllBalls.startX,AllBalls.startY);
            field.amountOfBalls= field.allBalls.getArrayBalls().size;
            field.next();
        }

            // AllShapes.shapes = gson.fromJson(preferences.getString("shapes"), Values.class).shapes;
    }
    public boolean contains(String s){return preferences.contains(s);}

    public void save(){preferences.flush();}
    public void clear(){preferences.clear(); preferences.flush();}
}
