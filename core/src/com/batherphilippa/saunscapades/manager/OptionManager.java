package com.batherphilippa.saunscapades.manager;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.batherphilippa.saunscapades.SaunScapades;
import com.batherphilippa.saunscapades.screen.GameState;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class OptionManager {

    public static void handlePauseClicked(VisTextButton btn, SaunScapades game) {
        btn.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("pause clicked");
                game.setGameState(GameState.PAUSED);
            }
        });
    }

    public static void handleUnPauseClicked(VisTextButton btn, SaunScapades game) {
        btn.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("resume game...");
                game.setGameState(GameState.RUNNING);
            }
        });
    }

}
