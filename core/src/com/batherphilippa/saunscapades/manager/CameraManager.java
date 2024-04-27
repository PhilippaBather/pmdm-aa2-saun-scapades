package com.batherphilippa.saunscapades.manager;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.batherphilippa.saunscapades.screen.constants.AppConstants.*;

/**
 * CameraManeger - la clase que maneja las cameras del juego.
 */
public class CameraManager {

    private final OrthographicCamera gameCam;
    private final OrthographicCamera hudCam;
    private final OrthographicCamera optionCam;
    private final Viewport gamePort;
    private final Viewport hudPort;
    private final Viewport optionPort;

    public CameraManager() {
        this.gameCam = new OrthographicCamera();
        this.hudCam = new OrthographicCamera();
        this.optionCam = new OrthographicCamera();
        this.gamePort = new FitViewport(VIEW_WIDTH / PPM, VIEW_HEIGHT / PPM, gameCam);
        this.hudPort = new FillViewport(VIEW_WIDTH, VIEW_HEIGHT, new OrthographicCamera());
        this.optionPort = new FitViewport(VIEW_WIDTH, VIEW_HEIGHT, new OrthographicCamera());
        setCameraPosition();
    }

    private void setCameraPosition() {
        // establece la posición en el medio de la pantalla
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
    }

    public OrthographicCamera getGameCam() {
        return gameCam;
    }

    public OrthographicCamera getHudCam() {
        return hudCam;
    }

    public OrthographicCamera getOptionCam() {
        return optionCam;
    }

    public Viewport getHudPort() {
        return hudPort;
    }

    public Viewport getOptionPort() {
        return optionPort;
    }

    public void resizeGamePort(int width, int height) {
        gamePort.update(width, height);
    }
    public void resizeHudPort(int width, int height) {
        hudPort.update(width, height);
    }
    public void resizeOptionPort(int width, int height) {
        optionPort.update(width, height);
    }



    public void update(float x) {
        getGameCam().position.x = x;
        getGameCam().update();
        this.gameCam.update();
    }
}
