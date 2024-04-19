package com.batherphilippa.saunscapades.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.batherphilippa.saunscapades.SaunScapades;
import com.batherphilippa.saunscapades.screen.GameScreen;
import com.batherphilippa.saunscapades.screen.GameState;
import com.batherphilippa.saunscapades.screen.MainMenu;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class OptionManager {

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

    public static void handleMainMenuClicked(VisTextButton btn, SaunScapades game, Screen screen) {
        btn.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                SaunScapades.setGameState(GameState.RUNNING);
                if (screen != null) {
                    screen.dispose();
                }
                game.setScreen(new MainMenu(game));

            }
        });
    }

    public static void handlePauseClicked(VisTextButton btn) {
        btn.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                SaunScapades.setGameState(GameState.PAUSED);
            }
        });
    }

    public static void handleUnPauseClicked(VisTextButton btn) {
        btn.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                SaunScapades.setGameState(GameState.RUNNING);
            }
        });
    }

    public static void handlePlayAgainClicked(VisTextButton btn, SaunScapades game, Screen screen) {
        btn.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                screen.dispose();
                game.setScreen(new GameScreen(game));
                game.reset();
            }
        });
    }

}
