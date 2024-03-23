package com.batherphilippa.saunscapades.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.batherphilippa.saunscapades.util.Constants.*;

public class CameraManager {

    private final OrthographicCamera gameCam;

    private final Viewport gamePort;

    public CameraManager() {
        this.gameCam = new OrthographicCamera();
        this.gamePort = new FitViewport(VIEW_WIDTH / PPM, VIEW_HEIGHT / PPM, gameCam);
        setCameraPosition();
    }

    private void setCameraPosition() {
        // set position in the middle fo the screen
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
    }

    public OrthographicCamera getGameCam() {
        return gameCam;
    }

    public void resizeGamePort(int width, int height) {
        gamePort.update(width, height);
    }
    public void update(float x) {
        getGameCam().position.x = x; // player.getB2Body().getPosition().x;
        getGameCam().update();

        this.gameCam.update();
    }
}
