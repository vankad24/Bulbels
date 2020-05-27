package com.bulbels.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.Random;

public class AudioManager {
    public Sound coin1, coin2, coin3, coin4, take_coin, buy;
    public Sound click, slide, crash_box, lose, puck, warning, next_turn;
    public Music music;
    public float volumeEffects, volumeMusic, volume;
    Random rand;

    public AudioManager() {
        rand = new Random();
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        coin1 =  Gdx.audio.newSound(Gdx.files.internal("sounds/coin1.mp3"));
        coin2 =  Gdx.audio.newSound(Gdx.files.internal("sounds/coin2.mp3"));
        coin3 =  Gdx.audio.newSound(Gdx.files.internal("sounds/coin3.mp3"));
        coin4 =  Gdx.audio.newSound(Gdx.files.internal("sounds/coin4.mp3"));
        take_coin =  Gdx.audio.newSound(Gdx.files.internal("sounds/take_coin.mp3"));
        click =  Gdx.audio.newSound(Gdx.files.internal("sounds/click.wav"));
        slide =  Gdx.audio.newSound(Gdx.files.internal("sounds/slide.mp3"));
        crash_box =  Gdx.audio.newSound(Gdx.files.internal("sounds/crash_box.mp3"));
        lose =  Gdx.audio.newSound(Gdx.files.internal("sounds/lose.mp3"));
        puck =  Gdx.audio.newSound(Gdx.files.internal("sounds/puck.mp3"));
        warning =  Gdx.audio.newSound(Gdx.files.internal("sounds/warning.mp3"));
        buy =  Gdx.audio.newSound(Gdx.files.internal("sounds/buy.mp3"));
        next_turn =  Gdx.audio.newSound(Gdx.files.internal("sounds/next_turn.mp3"));
    }

    public void coin(){
        switch (rand.nextInt(4)){
            case 0:
                coin1.play(getVolumeEffects());
                break;
            case 1:
                coin2.play(getVolumeEffects());
                break;
            case 2:
                coin3.play(getVolumeEffects());
                break;
            case 3:
                coin4.play(getVolumeEffects());
                break;
        }
    }
    public void crash_box(){
        crash_box.play(.5f*getVolumeEffects());
    }
    public void slide(){
        slide.play(.2f*getVolumeEffects());
    }

    public void click(){
        click.play(getVolumeEffects());
    }
    public void puck(){ puck.play(getVolumeEffects());}

    public float getVolumeMusic() { return .1f*volumeMusic*volume;}

    public float getVolumeEffects() { return volumeEffects*volume;}
}
