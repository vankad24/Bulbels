package com.bulbels.game;

import com.bulbels.game.utils.AndroidHelper;

public class MyThread extends Thread{
    AndroidHelper helper;

    public MyThread(AndroidHelper helper) {
        this.helper = helper;
    }

    @Override
    public void run() {
        super.run();

    }
}
