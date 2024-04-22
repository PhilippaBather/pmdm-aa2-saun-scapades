package com.batherphilippa.saunscapades.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

import static com.batherphilippa.saunscapades.manager.constants.ResourcesConstants.*;

public class GameScreen implements Screen {

    private final SaunScapades game;
    private final GameLevel currLevel;
    private final SpriteBatch batch;
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
        this.currLevel = SaunScapades.currGameLevel;
        this.resourceManager = new ResourceManager();

        this.batch = new SpriteBatch();

        this.camManager = new CameraManager();

        this.hud = new Hud(this.batch, this.camManager);

        this.b2WorldManager = new B2WorldManager(this, resourceManager, hud);
        this.spriteManager = new SpriteManager(resourceManager, batch, hud, b2WorldManager);
        this.pauseMenu = new PauseBackground(this.game);
        this.optionBar = new OptionBar(this.game, this.batch);
    }

    public SpriteManager getSpriteManager() {
        return this.spriteManager;
    }

    @Override
    public void show() {
        UIUtils.clearScreen();
        if (SaunScapades.currGameLevel == GameLevel.LEVEL_1) {
            resourceManager.playMusic(MUSIC_COUNTRYSIDE, true);
        } else {
            resourceManager.playMusic(MUSIC_SPACE, true);
        }
        optionBarStage = optionBar.getStage();
        pauseStage = pauseMenu.getStage();
        Gdx.input.setInputProcessor(new InputMultiplexer(optionBarStage, pauseStage));
    }

    @Override
    public void render(float delta) {
        UIUtils.clearScreen();

        if (SaunScapades.gameState == GameState.GAME_OVER) {
            resourceManager.stopMusic(MUSIC_COUNTRYSIDE);
            dispose();
            game.setScreen(new GameOverScreen(game));
        }

        if (SaunScapades.gameState == GameState.FINISHED) {
            resourceManager.stopMusic(MUSIC_SPACE);
            SaunScapades.score = hud.getScore();
            dispose();
            game.setScreen(new GameEndScreen(game));
        } else if (SaunScapades.currGameLevel != currLevel) {
            SaunScapades.score = hud.getScore();
            dispose();
            game.setScreen(new LevelEndScreen(game));
        } else if (!SaunScapades.getGameState().equals(GameState.PAUSED) && SaunScapades.gameState != GameState.GAME_OVER) {

            // render game map
            b2WorldManager.update(camManager.getGameCam());

            // camera
            // place before sprite manager (to see sprite): tell game batch to recognise where camera is in game world and only render what the camera can see
            batch.setProjectionMatrix(camManager.getGameCam().combined);
            camManager.update(spriteManager.getPlayerPosition());

            // sprites
            spriteManager.manageInput();
            spriteManager.update(delta);
            spriteManager.draw();

            // hud
            batch.setProjectionMatrix(camManager.getHudCam().combined);
            hud.draw();
            hud.updateTimer(delta);

            // barra de opciones
            batch.setProjectionMatrix(camManager.getOptionCam().combined);
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
        camManager.resizeHudPort(width, height);
        camManager.resizeOptionPort(width, height);
    }

    @Override
    public void pause() {
        SaunScapades.setGameState(GameState.PAUSED);
    }

    @Override
    public void resume() {
        SaunScapades.setGameState(GameState.RUNNING);
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        b2WorldManager.dispose();
        hud.dispose();
        optionBarStage.dispose();
        pauseStage.dispose();
        resourceManager.dispose();
    }
}
