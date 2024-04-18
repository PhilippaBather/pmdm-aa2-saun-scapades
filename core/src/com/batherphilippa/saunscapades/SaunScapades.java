package com.batherphilippa.saunscapades;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.batherphilippa.saunscapades.manager.B2WorldManager;
import com.batherphilippa.saunscapades.manager.CameraManager;
import com.batherphilippa.saunscapades.manager.ResourceManager;
import com.batherphilippa.saunscapades.manager.SpriteManager;
import com.batherphilippa.saunscapades.screen.GameScreen;
import com.batherphilippa.saunscapades.screen.GameState;
import com.batherphilippa.saunscapades.screen.scene.Hud;
import com.batherphilippa.saunscapades.screen.scene.OptionBar;

public class SaunScapades extends Game {
    public SpriteBatch batch;
    private B2WorldManager b2WorldManager;
    private CameraManager camManager;
    private Hud hud;
    private OptionBar optionBar;
    private ResourceManager resManager;
    private SpriteManager spriteManager;
    public GameState gameState;

    @Override
    public void create() {
        batch = new SpriteBatch();

        camManager = new CameraManager();
        hud = new Hud(this, batch);
        optionBar = new OptionBar(this, batch);

        resManager = new ResourceManager();
        b2WorldManager = new B2WorldManager(this, resManager, hud);
        spriteManager = new SpriteManager(this, batch);

        gameState = GameState.RUNNING;

        this.setScreen(new GameScreen(this));
    }

    public B2WorldManager getB2WorldManager() {
        return b2WorldManager;
    }

    public CameraManager getCamManager() {
        return camManager;
    }

    public Hud getHud() {
        return hud;
    }

    public OptionBar getOptionBar() {
        return optionBar;
    }

    public ResourceManager getResManager() {
        return resManager;
    }

    public SpriteManager getSpriteManager() {
        return spriteManager;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public int getTotalPoints() {
        return hud.getScore();
    }
    public void reset() {
        setGameState(GameState.RUNNING);
        this.hud = new Hud(this, batch);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
