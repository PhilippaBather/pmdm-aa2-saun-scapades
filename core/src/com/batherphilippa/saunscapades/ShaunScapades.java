package com.batherphilippa.saunscapades;

import com.badlogic.gdx.Game;
import com.batherphilippa.saunscapades.manager.CameraManager;
import com.batherphilippa.saunscapades.manager.ResourceManager;
import com.batherphilippa.saunscapades.screen.*;

public class ShaunScapades extends Game {
    private CameraManager camManager;
    private ResourceManager resManager;
    public static GameState gameState;
    public static GameLevel currGameLevel;
    public static GameLevel prevGameLevel;
    public static int score;

    @Override
    public void create() {
        gameState = GameState.RUNNING;
        currGameLevel = GameLevel.LEVEL_1;
        prevGameLevel = GameLevel.LEVEL_1;
        score = 0;

        this.camManager = new CameraManager();
        this.resManager = new ResourceManager();

        this.setScreen(new GameSplashScreen(this));
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
        ShaunScapades.gameState = gameState;
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

    }
}
