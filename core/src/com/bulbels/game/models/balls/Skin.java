package com.bulbels.game.models.balls;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Skin {
    TextureAtlas atlas;
    public boolean changed;
    private int skinId;

    public Skin(TextureAtlas atlas) {
        this.atlas = atlas;
    }

    Color color=Color.PINK;
    public class ColorSkin{
        public static final int WHITE=0;
        public static final int BLUE=1;
        public static final int ORANGE=2;
        public static final int GREEN=3;
        public static final int YELLOW=4;
        public static final int LIME=5;
        public static final int CYAN=6;
        public static final int RED=7;
        public static final int GOLD=8;
        public static final int PINK=9;
    }
    public class TextureSkin{
        public static final int FOOTBALL=20;
        public static final int BOWLING=21;
        public static final int BILLIARD=22;
        public static final int TENNIS=23;
        public static final int BASEBALL=24;
        public static final int BASKETBALL=25;
        public static final int COLORFUL=26;
        public static final int EARTH=27;
        public static final int ENDER_PERL =28;
        public static final int RAINBOW_FOOTBALL =29;
    }

    public Sprite getSprite(){return getSprite(skinId);}
    public Sprite getSprite(int skinId){
        Sprite sprite =null;
        if (skinId<20) {
            sprite = new Sprite(atlas.findRegion("ball"));
            switch (skinId) {
                case ColorSkin.WHITE:
                    break;
                case ColorSkin.BLUE:
                    sprite.setColor(Color.BLUE);
                    break;
                case ColorSkin.RED:
                    sprite.setColor(Color.RED);
                    break;
                case ColorSkin.GREEN:
                    sprite.setColor(Color.GREEN);
                    break;
                case ColorSkin.CYAN:
                    sprite.setColor(Color.CYAN);
                    break;
                case ColorSkin.LIME:
                    sprite.setColor(Color.LIME);
                    break;
                case ColorSkin.YELLOW:
                    sprite.setColor(Color.YELLOW);
                    break;
                case ColorSkin.ORANGE:
                    sprite.setColor(Color.ORANGE);
                    break;
                case ColorSkin.GOLD:
                    sprite.setColor(Color.GOLD);
                    break;
                case ColorSkin.PINK:
                    sprite.setColor(Color.PINK);
                    break;
            }
        }else {
            switch (skinId) {
                case TextureSkin.FOOTBALL:
                    sprite = new Sprite(atlas.findRegion("foot_ball"));
                    break;
                case TextureSkin.BILLIARD:
                    sprite = new Sprite(atlas.findRegion("billiard_ball"));
                    break;
                case TextureSkin.BOWLING:
                    sprite = new Sprite(atlas.findRegion("bawling_ball"));
                    break;
                case TextureSkin.TENNIS:
                    sprite = new Sprite(atlas.findRegion("tennis_ball"));
                    break;
                case TextureSkin.BASKETBALL:
                    sprite = new Sprite(atlas.findRegion("basket_ball"));
                    break;
                case TextureSkin.BASEBALL:
                    sprite = new Sprite(atlas.findRegion("baseball"));
                    break;
                case TextureSkin.RAINBOW_FOOTBALL:
                    sprite = new Sprite(atlas.findRegion("rainbow_football"));
                    break;
                case TextureSkin.COLORFUL:
                    sprite = new Sprite(atlas.findRegion("colorful_ball"));
                    break;
                case TextureSkin.EARTH:
                    sprite = new Sprite(atlas.findRegion("earth"));
                    break;
                case TextureSkin.ENDER_PERL:
                    sprite = new Sprite(atlas.findRegion("ender_perl"));
                    break;
            }
        }
        return sprite;
    }

    public void setSkinId(int skinId) {
        this.skinId = skinId;
        changed = true;
    }

    public int getSkinId() { return skinId; }
}
