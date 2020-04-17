package com.bulbels.game.screens;

import com.badlogic.gdx.Screen;

import java.util.Stack;

public interface ScreenManager {
    Stack<MyScreen> SCREENS = new Stack<>();
    MyScreen add(MyScreen screen);
    MyScreen remove();
    void back();
}
