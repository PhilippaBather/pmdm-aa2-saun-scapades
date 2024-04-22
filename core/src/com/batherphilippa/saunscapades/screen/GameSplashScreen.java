package com.batherphilippa.saunscapades.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.batherphilippa.saunscapades.SaunScapades;
import com.batherphilippa.saunscapades.manager.ResourceManager;
import com.batherphilippa.saunscapades.screen.util.UIUtils;

import javax.swing.text.Utilities;

import static com.batherphilippa.saunscapades.manager.constants.ResourcesConstants.IMAGE_SPLASH;

public class GameSplashScreen implements Screen {

    private final SaunScapades game;
    private final ResourceManager resManager;
    private final Stage stage;
    private final Image splashImg;
    private final Texture splashTexture;
    private boolean splashDone;

    public GameSplashScreen(SaunScapades game) {
        this.game = game;
        this.resManager = this.game.getResManager();

        this.splashTexture = new Texture(resManager.getSplashImg(IMAGE_SPLASH));
        this.splashImg = new Image(this.splashTexture);
        this.splashDone = false;
        this.stage = new Stage();

    }

    @Override
    public void show() {
        UIUtils.clearScreen();

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        splashImg.addAction(Actions.sequence(Actions.alpha(0),
                Actions.fadeIn(1.5f),
                Actions.delay(2.5f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        splashDone = true;
                    }
                })));

        table.row().height(splashTexture.getHeight());
        table.add(splashImg).center();
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        UIUtils.clearScreen();

        stage.act();
        stage.draw();

        if (splashDone) {
            dispose();
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {

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
        splashImg.clear();
        splashTexture.dispose();
        stage.dispose();
    }
}
