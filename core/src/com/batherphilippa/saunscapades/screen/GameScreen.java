package com.batherphilippa.saunscapades.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.batherphilippa.saunscapades.SaunScapades;
import com.batherphilippa.saunscapades.manager.B2WorldManager;
import com.batherphilippa.saunscapades.manager.CameraManager;
import com.batherphilippa.saunscapades.manager.ResourceManager;
import com.batherphilippa.saunscapades.manager.SpriteManager;
import com.batherphilippa.saunscapades.screen.scene.Hud;
import com.batherphilippa.saunscapades.screen.scene.OptionBar;
import com.batherphilippa.saunscapades.screen.scene.PauseBackground;
import com.batherphilippa.saunscapades.screen.util.UIUtils;

public class GameScreen implements Screen {

    private final SaunScapades game;
    private final B2WorldManager b2WorldManager;
    private final CameraManager camManager;
    private final ResourceManager resourceManager;
    private final SpriteManager spriteManager;
    private final Hud hud;
    private final OptionBar optionBar;
    private final PauseBackground pauseMenu;

    // opciones de la tabla
    private Stage optionBarStage;
    private Stage pauseStage;

    public GameScreen(SaunScapades game) {
        this.game = game;
        this.b2WorldManager = this.game.getB2WorldManager();
        this.camManager = this.game.getCamManager();
        this.resourceManager = this.game.getResManager();
        this.spriteManager = this.game.getSpriteManager();
        this.hud = this.game.getHud();
        this.optionBar = this.game.getOptionBar();
        this.optionBarStage = new Stage();
        this.pauseMenu = new PauseBackground(this.game);
    }

    @Override
    public void show() {
        UIUtils.clearScreen();

        resourceManager.playMusic("countryside");

        optionBarStage = optionBar.getStage();
        pauseStage = pauseMenu.getStage();
        Gdx.input.setInputProcessor(new InputMultiplexer(optionBarStage, pauseStage));
    }

    @Override
    public void render(float delta) {
        UIUtils.clearScreen();

        if(game.gameState == GameState.GAME_OVER) {
            resourceManager.stopMusic("countryside");
            game.setScreen(new GameOver(game));
        }

        if (!game.getGameState().equals(GameState.PAUSED) && game.gameState != GameState.GAME_OVER) {

            // render game map
            b2WorldManager.update(camManager.getGameCam());

            // camera
            // place before sprite manager (to see sprite): tell game batch to recognise where camera is in game world and only render what the camera can see
            game.batch.setProjectionMatrix(camManager.getGameCam().combined);
            camManager.update(spriteManager.getPlayerPosition());

            // sprites
            spriteManager.manageInput();
            spriteManager.update(delta);
            spriteManager.draw();

            // hud
            game.batch.setProjectionMatrix(camManager.getHudCam().combined);
            hud.draw();
            hud.updateTimer(delta);

            // barra de opciones
            game.batch.setProjectionMatrix(camManager.getOptionCam().combined);
            optionBarStage.act();
            optionBarStage.draw();

        } else {
            pauseStage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        // required to resize port to see tiled map
        camManager.resizeGamePort(width, height);
        camManager.resizeHudPort(width, height);  // TODO - why do I need this for the btn click to work?
        camManager.resizeOptionPort(width, height);
    }

    @Override
    public void pause() {
        game.setGameState(GameState.PAUSED);
    }

    @Override
    public void resume() {
        game.setGameState(GameState.RUNNING);
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        b2WorldManager.dispose();
        hud.dispose();
        game.dispose();
        optionBar.dispose();
        pauseMenu.dispose();
        pauseStage.dispose();
        optionBarStage.dispose();
        resourceManager.dispose();
        spriteManager.dispose();
    }
}
