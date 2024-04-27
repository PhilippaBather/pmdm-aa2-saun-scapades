package com.batherphilippa.saunscapades.screen;

/**
 * GameLevel - define el nivel del juego.
 */
public enum GameLevel {
    LEVEL_1(1),
    LEVEL_2(2);

    private int level;
    GameLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
