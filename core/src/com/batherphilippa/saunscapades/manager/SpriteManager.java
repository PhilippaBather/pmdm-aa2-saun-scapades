package com.batherphilippa.saunscapades.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.batherphilippa.saunscapades.SaunScapades;
import com.batherphilippa.saunscapades.domain.sprite.Shaun;
import com.batherphilippa.saunscapades.util.UserInput;

import static com.batherphilippa.saunscapades.domain.sprite.SpritePositionState.IDLE;
import static com.batherphilippa.saunscapades.domain.sprite.SpritePositionState.MOVING;

public class SpriteManager implements Disposable {

    private final SaunScapades game;
    private final B2WorldManager b2WorldManager;
    private final ResourceManager resManager;
    private final SpriteBatch batch;
    private Shaun player;
    private float timeToNextLoop = 2;


    public SpriteManager(SaunScapades game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
        this.b2WorldManager = this.game.getB2WorldManager();
        this.resManager = this.game.getResManager();

        init();
    }

    /**
     * Inicializar a los sprites.
     */
    private void init() {
        this.player = new Shaun(resManager.loadRegion("shaun_walk", 0), b2WorldManager.getWorld(), 32, 38, 6, this);
    }

    public TextureRegion getTextureRegion(String name, int index) {
        return resManager.loadRegion(name, index);
    }

    public float getPlayerPosition() {
        return player.getB2Body().getPosition().x;
    }

    public void update(float dt) {
        this.player.update(dt);
    }

    public void manageInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.move(UserInput.UP);
            resManager.playSound("shaun_jump");
        }
        // not just pressed as want to know if the user is holding the key down
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.getLinearVelocity() <= 2) {
            player.move(UserInput.RIGHT);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.getLinearVelocity() >= -2) {
            player.move(UserInput.LEFT);
        }
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
