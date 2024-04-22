package com.batherphilippa.saunscapades;

import com.badlogic.gdx.Game;
import com.batherphilippa.saunscapades.manager.CameraManager;
import com.batherphilippa.saunscapades.manager.ResourceManager;
import com.batherphilippa.saunscapades.screen.GameState;
import com.batherphilippa.saunscapades.screen.MainMenuScreen;

public class SaunScapades extends Game {
    private CameraManager camManager;
    private ResourceManager resManager;
    public static GameState gameState;

    @Override
    public void create() {
        gameState = GameState.RUNNING;

        this.camManager = new CameraManager();
        this.resManager = new ResourceManager();

        this.setScreen(new MainMenuScreen(this));
    }

    public CameraManager getCamManager() {
        return camManager;
    }

    public ResourceManager getResManager() {
        return resManager;
    }
    public static GameState getGameState() {
        return gameState;
    }

    public static void setGameState(GameState gameState) {
        SaunScapades.gameState = gameState;
    }

    public void reset() {
        setGameState(GameState.RUNNING);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        resManager.dispose();
    }
}
