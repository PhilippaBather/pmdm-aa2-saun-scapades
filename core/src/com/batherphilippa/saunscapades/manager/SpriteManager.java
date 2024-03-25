package com.batherphilippa.saunscapades.manager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.batherphilippa.saunscapades.SaunScapades;
import com.batherphilippa.saunscapades.domain.Shaun;

public class SpriteManager implements Disposable {

    private final SaunScapades game;
    private final B2WorldManager b2WorldManager;
    private final SpriteBatch batch;
    private Shaun player;

    public SpriteManager(SaunScapades game, B2WorldManager b2WorldManager, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
        this.b2WorldManager = b2WorldManager;

        init();
    }

    /**
     * Inicializar a los sprites.
     */
    private void init() {
        this.player = new Shaun(b2WorldManager.getWorld(), 32, 38, 6);  // TODO - revise version to include texture
    }

    public void update() {}

    public void manageInput() {

    }

    public void draw() {
        batch.begin();
        player.render(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        b2WorldManager.dispose();
        game.dispose();
        player.dispose();
    }
}
