package com.batherphilippa.saunscapades.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisList;

import static com.batherphilippa.saunscapades.GameConstants.APP_NAME;
import static com.batherphilippa.saunscapades.screen.constants.ConfigConstants.*;

public class ConfigManager {

    private static final Preferences prefs = Gdx.app.getPreferences(APP_NAME);

    public static void handleGameLengthPref(VisList gameLengthList) {
        gameLengthList.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                float len = switch (gameLengthList.getSelectedIndex()) {
                    case 0 -> GAME_LENGTH_SHORT;
                    case 1 -> GAME_LENGTH_MEDIUM;
                    case 2 -> GAME_LENGTH_LONG;
                    default ->
                            throw new IllegalStateException("Unexpected value: " + gameLengthList.getSelectedIndex());
                };
                prefs.putFloat("game_length", len);
                prefs.flush();
            }
        });
    }

    public static float getGameLength() {
        return prefs.getFloat("game_length");
    }

    public static String getGameLengthOption() {
        float selected = getGameLength();
        return switch((int) selected) {
            case (int) GAME_LENGTH_MEDIUM -> GAME_LENGTH_STR_MEDIUM;
            case (int) GAME_LENGTH_LONG -> GAME_LENGTH_STR_LONG;
            default -> GAME_LENGTH_STR_SHORT;
        };
    }

    public static void handleGameDifficultyPref(VisList gameDiffList) {
        gameDiffList.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                switch (gameDiffList.getSelectedIndex()) {
                    case 0 -> setGameEasyPref();
                    case 1 -> setGameMediumPref();
                    case 2 -> setGameHardPref();
                    default ->
                            throw new IllegalStateException("Unexpected value: " + gameDiffList.getSelectedIndex());
                };
            }
        });
    }

    private static void setGameEasyPref() {
        prefs.putFloat("game_lives", 5);
        prefs.putFloat("game_energy", 6);
        prefs.flush();
    }

    private static void setGameMediumPref() {
        prefs.putFloat("game_lives", 3);
        prefs.putFloat("game_energy", 4);
        prefs.flush();
    }

    private static void setGameHardPref() {
        prefs.putFloat("game_lives", 1);
        prefs.putFloat("game_energy", 2);
        prefs.flush();
    }

    public static float getGameLives() {
        return prefs.getFloat("game_lives");
    }

    public static float getGameEnergy() {
        return prefs.getFloat("game_energy");
    }

}