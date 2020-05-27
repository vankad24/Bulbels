package com.bulbels.game.utils;

public class ShapeData {
    public int type,health, maxHealth;
    public float x, y;

    public ShapeData(int type, int health, int maxHealth, float x, float y) {
        this.type = type;
        this.health = health;
        this.maxHealth = maxHealth;
        this.x = x;
        this.y = y;
    }
}
