package com.bulbels.game.utils;

import com.badlogic.gdx.Screen;
import com.bulbels.game.screens.MyScreen;

import java.util.Stack;

public interface ScreenManager {
    Stack<MyScreen> SCREENS = new Stack<>();
    MyScreen add(MyScreen screen);
    MyScreen remove();
    void back();
}
