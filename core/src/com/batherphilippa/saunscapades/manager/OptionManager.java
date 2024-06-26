package com.batherphilippa.saunscapades.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.batherphilippa.saunscapades.ShaunScapades;
import com.batherphilippa.saunscapades.screen.*;
import com.kotcrab.vis.ui.widget.VisTextButton;

/**
 * OptionManager - la clase que es responsable de manejar los selecciones de bótones por el usuario.
 */
public class OptionManager {

    public static void handleConfigMenuClicked(VisTextButton btn, ShaunScapades game, Screen screen) {
        btn.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                screen.dispose();
                game.setScreen(new ConfigScreen(game));

            }
        });
    }

    public static void handleExitClicked(VisTextButton btn, Screen screen) {
        btn.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (screen != null) {
                    screen.dispose();
                }
                Gdx.app.log("Exit", "Game exiting...");
                System.exit(0);
            }
        });
    }

    public static void handleMainMenuClicked(VisTextButton btn, ShaunScapades game, Screen screen) {
        btn.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                screen.dispose();
                game.setScreen(new MainMenuScreen(game));

            }
        });
    }

    public static void handlePauseClicked(VisTextButton btn) {
        btn.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                ShaunScapades.setGameState(GameState.PAUSED);
            }
        });
    }

    public static void handleUnPauseClicked(VisTextButton btn) {
        btn.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                ShaunScapades.setGameState(GameState.RUNNING);
            }
        });
    }

    public static void handlePlayClicked(VisTextButton btn, ShaunScapades game, Screen screen) {
        btn.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                screen.dispose();
                ShaunScapades.currGameLevel = GameLevel.LEVEL_1;
                game.reset();
                game.setScreen(new LevelOneInfoScreen(game));
            }
        });
    }

    public static void handlePlayNewLevelClicked(VisTextButton btn, ShaunScapades game, Screen screen) {
        btn.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                screen.dispose();
                game.setScreen(new GameScreen(game));
            }
        });
    }

    public static void handleTopTenClicked(VisTextButton btn, ShaunScapades game, Screen screen) {
        btn.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                screen.dispose();
                game.setScreen(new TopTenScreen(game));
            }
        });
    }
}
