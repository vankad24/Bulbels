package com.bulbels.game.utils;

import java.util.TreeMap;

public interface AndroidHelper {
    public TreeMap<String, String> STRINGS = new TreeMap<>();
    void printMessage(String title, String text);
    void makeToast(String text);

    String getString(String name);
}
