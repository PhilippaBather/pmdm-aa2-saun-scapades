package com.batherphilippa.saunscapades.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.batherphilippa.saunscapades.SaunScapades;
import com.batherphilippa.saunscapades.domain.sprite.*;
import com.batherphilippa.saunscapades.screen.scene.Hud;
import com.batherphilippa.saunscapades.util.UserInput;

public class SpriteManager implements Disposable {

    private final SaunScapades game;
    private final B2WorldManager b2WorldManager;
    private final ResourceManager resManager;
    private final SpriteBatch batch;
    private final Hud hud;
    private Shaun player;
    private Shirley shirleySheep;
    private Array<AngrySheep> angrySheepArray;
    private Array<Bomb> bombArray;

    public SpriteManager(SaunScapades game, SpriteBatch batch, Hud hud) {
        this.game = game;
        this.batch = batch;
        this.b2WorldManager = this.game.getB2WorldManager();
        this.resManager = this.game.getResManager();
        this.hud = hud;

        init();
    }

    /**
     * Inicializar a los sprites.
     */
    private void init() {
        this.player = new Shaun(resManager.loadRegion("shaun_idle", -1), b2WorldManager.getWorld(), 32, 38, 8, this);
        this.angrySheepArray = b2WorldManager.renderAngrySheep(resManager.loadRegion("black_sheep_run", 1), this);
        this.bombArray = b2WorldManager.renderBombs(resManager.loadRegion("bomb_idle", -1), this);
        this.shirleySheep = b2WorldManager.rendLevelEndObject(resManager.loadRegion("shirley_end_level", -1), this);
    }

    public TextureRegion getTextureRegion(String name, int index) {
        return resManager.loadRegion(name, index);
    }

    public float getPlayerPosition() {
        return player.getB2Body().getPosition().x;
    }

    public void update(float dt) {
        this.player.update(dt);
        this.shirleySheep.update(dt);
        for (AngrySheep angrySheep : angrySheepArray) {
            angrySheep.update(dt);
        }
        for (Bomb bomb : bombArray) {
            bomb.update(dt);
        }
    }

    public void manageInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && player.getB2Body().getLinearVelocity().y == 0) {
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
        shirleySheep.draw(batch);
        for (AngrySheep angrySheep : angrySheepArray) {
            angrySheep.render(batch);
        }
        for (Bomb bomb : bombArray) {
            bomb.render(batch);
        }
        batch.end();
    }

    public void schedulePlayerRestart(int delay) {
        Timer.Task task = new Timer.Task() {
            @Override
            public void run() {
                restartPlayer();
            }
        };

        Timer.instance().scheduleTask(task, delay);
    }

    private void restartPlayer() {
        if (!b2WorldManager.getWorld().isLocked() && player.isHasLostLife()) {
            resManager.playSound("teleportdown");
            b2WorldManager.getWorld().destroyBody(player.getB2Body());
            player = null;
            this.player = new Shaun(resManager.loadRegion("shaun_idle", -1), b2WorldManager.getWorld(), 32, 38, 8, this);
        }
        resManager.playMusic("countryside", 2);
    }

    public void updateScore(int score) {
        this.hud.updateScore(score);
    }

    public void playerHit(SpriteType npc, int delay) {
        resManager.playSound("sheep_death_no");

        if (npc == SpriteType.ENEMY) {
            this.hud.updateEnergy(-2);
            if (this.hud.getEnergy() <= 0) {
                playerKilled(delay);
            }
        }

        if (npc == SpriteType.BOMB) {
            this.player.launchShaun();
            resManager.playSound("explosion");
        }

        if (npc == SpriteType.BOMB || npc == SpriteType.OBJECT) {
            playerKilled(delay);
        }
    }

    private void playerKilled(int delay) {
        this.player.resetState(SpriteState.DEAD);
        schedulePlayerRestart(delay);
        this.hud.updateLives(-1);
    }

    public void enemyHit() {
        resManager.playSound("enemy_death");
        resManager.playSound("sheep_victory");
        updateScore(500);
    }

    public void levelEndCelebration() {
        this.player.resetState(SpriteState.VICTORY);
        updateScore(1000);
    }

    @Override
    public void dispose() {
        batch.dispose();
        b2WorldManager.dispose();
        angrySheepArray.clear();
        bombArray.clear();
        game.dispose();
        player.dispose();
    }
}
