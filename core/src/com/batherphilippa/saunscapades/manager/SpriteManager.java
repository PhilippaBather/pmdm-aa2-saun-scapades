package com.batherphilippa.saunscapades.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.batherphilippa.saunscapades.ShaunScapades;
import com.batherphilippa.saunscapades.domain.sprite.*;
import com.batherphilippa.saunscapades.screen.GameLevel;
import com.batherphilippa.saunscapades.screen.GameState;
import com.batherphilippa.saunscapades.screen.scene.Hud;
import com.batherphilippa.saunscapades.util.UserInput;

import static com.batherphilippa.saunscapades.ShaunScapades.currGameLevel;
import static com.batherphilippa.saunscapades.ShaunScapades.setGameState;
import static com.batherphilippa.saunscapades.manager.constants.ResourcesConstants.*;
import static com.batherphilippa.saunscapades.screen.constants.PointsConstants.*;
import static com.batherphilippa.saunscapades.util.Constants.PPM;

public class SpriteManager implements Disposable {

    private final B2WorldManager b2WorldManager;
    private final ResourceManager resManager;
    private final SpriteBatch batch;
    private final Hud hud;
    private Shaun player;
    private Balloons balloons;
    private Array<Bomb> bombArr;
    private Array<AngrySheep> angrySheepArr;
    private Array<KamizakeSheep> kamizakeSheepArr;
    private Array<TrappedSheep> trappedSheepArr;

    public SpriteManager(ResourceManager resManager, SpriteBatch batch, Hud hud, B2WorldManager b2WorldManager) {
        this.batch = batch;
        this.b2WorldManager = b2WorldManager;
        this.resManager = resManager;
        this.hud = hud;

        init();
    }

    /**
     * Inicializa a los sprites.
     */
    private void init() {
        this.player = new Shaun(resManager.loadRegion("shaun_idle", -1), b2WorldManager.getWorld(), 32, 38, 8, this);
        this.balloons = b2WorldManager.renderLevelEndSheep(resManager.loadRegion("balloons", -1), this);
        this.angrySheepArr = b2WorldManager.renderAngrySheep(resManager.loadRegion("black_sheep_run", 1), this);
        this.bombArr = b2WorldManager.renderBombs(resManager.loadRegion("bomb_idle", -1), this);

        if (currGameLevel == GameLevel.LEVEL_2) {
            this.trappedSheepArr = b2WorldManager.renderTrappedSheep(resManager.loadRegion("shirley_end_level", -1), this);
            this.kamizakeSheepArr = b2WorldManager.renderFallingSheep(resManager.loadRegion("timmy_idle", -1), this);
        }
    }

    public TextureRegion getTextureRegion(String name, int index) {
        return resManager.loadRegion(name, index);
    }

    public float getPlayerPosition() {
        return player.getB2Body().getPosition().x;
    }

    public void update(float dt) {
        this.player.update(dt);
        this.balloons.update(dt);

        for (AngrySheep angrySheep : angrySheepArr) {
            angrySheep.update(dt);
        }

        for (Bomb bomb : bombArr) {
            if (bomb.getX() < player.getX() + 224 / PPM) {
                bomb.update(dt);
            }
        }

        if (currGameLevel == GameLevel.LEVEL_2) {
            for (TrappedSheep sheep : trappedSheepArr) {
                sheep.update(dt);
            }
            for (KamizakeSheep sheep : kamizakeSheepArr) {
                if (sheep.getX() < player.getX() + 10 / PPM) {
                    sheep.update(dt);
                }
            }
        }

    }

    public void manageInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && player.getB2Body().getLinearVelocity().y == 0) {
            player.move(UserInput.UP);
            resManager.playSound(SOUND_SHAUN_JUMP);
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
        balloons.draw(batch);
        for (AngrySheep angrySheep : angrySheepArr) {
            angrySheep.render(batch);
        }
        for (Bomb bomb : bombArr) {
            bomb.render(batch);
        }

        if (currGameLevel == GameLevel.LEVEL_2) {
            for (TrappedSheep sheep : trappedSheepArr) {
                sheep.render(batch);
            }
            for (KamizakeSheep sheep : kamizakeSheepArr) {
                sheep.render(batch);
            }
        }

        batch.end();
    }

    public void updateScore(int points) {
        hud.updateScore(points);
    }

    public void enemyKilled() {
        resManager.playSound(SOUND_ENEMY_DEATH);
        resManager.playSound(SOUND_SHAUN_CELEBRATION);
        hud.updateScore(POINTS_ENEMY_KILLED);
    }

    public void playerHit(SpriteType npc, int delay) {
        resManager.playSound(SOUND_SHAUN_DEATH_NOO);

        switch (npc) {
            case ENEMY -> handleEnemyHit(delay);
            case BOMB -> handleBombHit(delay);
            case OBJECT -> handleObjectHit(delay);
        }
    }

    private void handleEnemyHit(int delay) {
        hud.updateEnergy(-2);
        if (hud.getEnergy() <= 0) {
            hud.stopTimer();
            playerKilled(delay);
        }
    }

    private void handleBombHit(int delay) {
        player.launchShaun();
        resManager.playSound(SOUND_EXPLOSION);
        handleObjectHit(delay);
    }

    private void handleObjectHit(int delay) {
        String music = currGameLevel == GameLevel.LEVEL_1 ? MUSIC_COUNTRYSIDE : MUSIC_SPACE;
        resManager.stopMusic(music);
        hud.stopTimer();
        playerKilled(delay);
    }

    public void allyHit() {
        resManager.playSound(SOUND_EXPLOSION);
        resManager.playSound(SOUND_SHIRLEY_DEATH_NOO);
        resManager.playSound(SOUND_FALLING_SHEEP_DEATH);
    }

    public void handleEnemyHitByBomb() {
        resManager.playSound(SOUND_EXPLOSION);
        resManager.playSound(SOUND_SHAUN_CELEBRATION);
        resManager.playSound(SOUND_ENEMY_DEATH);
    }

    private void playerKilled(int delay) {
        player.resetState(SpriteState.DEAD);
        hud.updateLives(-1);

        setGameState(hud.getLives() <= 0 ? GameState.GAME_OVER : GameState.RUNNING);

        if (ShaunScapades.getGameState() != GameState.GAME_OVER) {
            String music = currGameLevel == GameLevel.LEVEL_1 ? MUSIC_COUNTRYSIDE : MUSIC_SPACE;
            resManager.playMusic(music, true);
            schedulePlayerRestart(delay);
        }
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
        if (hud.getEnergy() <= 0) {
            hud.updateEnergy(4);
        }
        resetPlayer();
        hud.resetWorldTimer();
    }

    public void resetPlayer() {
        if (!b2WorldManager.getWorld().isLocked() && player.isHasLostLife()) {
            b2WorldManager.getWorld().destroyBody(player.getB2Body());
            player = null;
            player = new Shaun(resManager.loadRegion("shaun_idle", -1), b2WorldManager.getWorld(), 32, 38, 8, this);
        }
    }

    public void handleSheepDeath() {
        resManager.playSound(SOUND_SHIRLEY_DEATH_NOO);
        resManager.playSound(SOUND_SHAUN_DEATH_NOO);
        resManager.playSound(SOUND_EXPLOSION);
        hud.updateScore(POINTS_SHEEP_DEATH);
    }

    public void handleSavedSheep() {
        resManager.playSound(SOUND_SHIRLEY_CELEBRATION);
        resManager.playSound(SOUND_SHAUN_CELEBRATION);
        hud.updateScore(POINTS_SHEEP_SAVED);
    }

    public void levelEndCelebration() {
        player.resetState(SpriteState.VICTORY);
        String music = currGameLevel == GameLevel.LEVEL_1 ? MUSIC_COUNTRYSIDE : MUSIC_SPACE;
        resManager.stopMusic(music);
        resManager.playMusic(MUSIC_LEVEL_END, true);
        hud.updateScore(POINTS_LEVEL_VICTORY);
        Timer.Task task = new Timer.Task() {
            @Override
            public void run() {
                resManager.stopMusic(MUSIC_LEVEL_END);
                if (ShaunScapades.currGameLevel == GameLevel.LEVEL_1) {
                    ShaunScapades.currGameLevel = GameLevel.LEVEL_2;
                } else {
                    ShaunScapades.setGameState(GameState.FINISHED);
                }
            }
        };
        Timer.instance().scheduleTask(task, 4);
    }

    public void handleFallingSheep() {
        resManager.playSound(SOUND_TIMMY_TRAMPOLINE);
    }

    public void handleParalysedShaun() {
        handleFallingSheep();
        resManager.playSound(SOUND_SHAUN_PARALYSED);
        player.resetState(SpriteState.PARALYSED);
    }

    @Override
    public void dispose() {
        batch.dispose();
        angrySheepArr.clear();
        bombArr.clear();
        kamizakeSheepArr.clear();
        trappedSheepArr.clear();
        b2WorldManager.dispose();
        hud.dispose();
        resManager.dispose();
        player.dispose();
        balloons.dispose();
    }

}
