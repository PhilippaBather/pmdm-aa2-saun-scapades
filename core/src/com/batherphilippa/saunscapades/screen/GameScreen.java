package com.batherphilippa.saunscapades.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.batherphilippa.saunscapades.SaunScapades;
import com.batherphilippa.saunscapades.manager.B2WorldManager;
import com.batherphilippa.saunscapades.manager.CameraManager;
import com.batherphilippa.saunscapades.manager.SpriteManager;

public class GameScreen implements Screen {

    private final SaunScapades game;
    private final B2WorldManager b2WorldManager;
    private final CameraManager camManager;
    private final SpriteManager spriteManager;

    public GameScreen(SaunScapades game) {
        this.game = game;
        this.b2WorldManager = this.game.getB2WorldManager();
        this.camManager = this.game.getCamManager();
        this.spriteManager = this.game.getSpriteManager();
    }

    @Override
    public void show() {
        // colour
        Gdx.gl.glClearColor(0, 0, 0, 1);
        // clears screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }


    @Override
    public void render(float delta) {
        // colour
        Gdx.gl.glClearColor(0, 0, 0, 1);
        // clears screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // render game map
        b2WorldManager.update(camManager.getGameCam());

        // place before sprite manager (to see sprite): tell game batch to recognise where camera is in game world and only render what the camera can see
        game.batch.setProjectionMatrix(camManager.getGameCam().combined);
        // camera
        camManager.update(spriteManager.getPlayerPosition());

        // sprites
        spriteManager.manageInput();
        spriteManager.update(delta);
        spriteManager.draw();
    }

    @Override
    public void resize(int width, int height) {
        // required to resize port to see tiled map
        camManager.resizeGamePort(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        game.dispose();
    }
}
