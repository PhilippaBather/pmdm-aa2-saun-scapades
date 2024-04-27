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
import com.batherphilippa.saunscapades.screen.util.UserInput;

import static com.batherphilippa.saunscapades.ShaunScapades.currGameLevel;
import static com.batherphilippa.saunscapades.ShaunScapades.setGameState;
import static com.batherphilippa.saunscapades.manager.constants.ResourcesConstants.*;
import static com.batherphilippa.saunscapades.screen.constants.PointsConstants.*;
import static com.batherphilippa.saunscapades.screen.constants.AppConstants.PPM;

/**
 * SpriteManager - la clase responable de manejar las acciones de Sprites; implementa Disposable.
 */
public class SpriteManager implements Disposable {

    private final B2WorldManager b2WorldManager;
    private final ResourceManager resManager;
    private final SpriteBatch batch;
    private final Hud hud;
    private Shaun player;
    private Balloons balloons;
    private Array<Bomb> bombArr;
    private Array<AngrySheep> angrySheepArr;
    private Array<KamikazeSheep> kamizakeSheepArr;
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
        this.balloons = b2WorldManager.renderLevelEndBalloons(resManager.loadRegion("balloons", -1), this);
        this.angrySheepArr = b2WorldManager.renderAngrySheep(resManager.loadRegion("black_sheep_run", 1), this);
        this.bombArr = b2WorldManager.renderBombs(resManager.loadRegion("bomb_idle", -1), this);

        if (currGameLevel == GameLevel.LEVEL_2) {
            this.trappedSheepArr = b2WorldManager.renderTrappedSheep(resManager.loadRegion("shirley_end_level", -1), this);
            this.kamizakeSheepArr = b2WorldManager.renderFallingSheep(resManager.loadRegion("timmy_idle", -1), this);
        }
    }

    /**
     * Devuelve la región de una textura
     * @param name - nombre de la textura
     * @param index - índice de la textura
     * @return la región de la textura
     */
    public TextureRegion getTextureRegion(String name, int index) {
        return resManager.loadRegion(name, index);
    }

    /**
     * Devuelve la posición del jugador.
     * @return
     */
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
            for (KamikazeSheep sheep : kamizakeSheepArr) {
                if (sheep.getX() < player.getX() + 10 / PPM) {
                    sheep.update(dt);
                }
            }
        }
    }

    /**
     * Maneja la entrada del usuario.
     */
    public void manageInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && player.getB2Body().getLinearVelocity().y == 0) {
            player.move(UserInput.UP);
            resManager.playSound(SOUND_SHAUN_JUMP);
        }
        // isKeyPressed para saber si el usuario está manteniendo pulsado el botón
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
            for (KamikazeSheep sheep : kamizakeSheepArr) {
                sheep.render(batch);
            }
        }

        batch.end();
    }

    /**
     * Actualiza la punctuación en el HUD
     * @param points
     */
    public void updateScore(int points) {
        hud.updateScore(points);
    }

    /**
     * Maneja las acciones cuando un inimgo está matado.
     */
    public void enemyKilled() {
        resManager.playSound(SOUND_ENEMY_DEATH);
        resManager.playSound(SOUND_SHAUN_CELEBRATION);
        hud.updateScore(POINTS_ENEMY_KILLED);
    }

    /**
     * Maneja las acciones cuando el jugador está golpeado.
     * @param npc - non-playable character
     * @param delay - retraso
     */
    public void playerHit(SpriteType npc, int delay) {
        resManager.playSound(SOUND_SHAUN_DEATH_NOO);

        switch (npc) {
            case ENEMY -> handleEnemyHit(delay);
            case BOMB -> handleBombHit(delay);
            case OBJECT -> handleObjectHit(delay);
        }
    }

    /**
     * Maneja las acciones cuando un inimigo está golpeado.
     * @param delay
     */
    private void handleEnemyHit(int delay) {
        hud.updateEnergy(-2);
        if (hud.getEnergy() <= 0) {
            hud.stopTimer();
            playerKilled(delay);
        }
    }

    /**
     * Maneja las acciones cuando el jugador está golpeado por una bomba detonando.
     * @param delay - retraso
     */
    private void handleBombHit(int delay) {
        player.launchShaun();
        resManager.playSound(SOUND_EXPLOSION);
        handleObjectHit(delay);
    }

    /**
     * Maneja las acciones cuando el jugador está golpeado y matado.
     * @param delay - retraso
     */
    private void handleObjectHit(int delay) {
        String music = currGameLevel == GameLevel.LEVEL_1 ? MUSIC_COUNTRYSIDE : MUSIC_SPACE;
        resManager.stopMusic(music);
        hud.stopTimer();
        playerKilled(delay);
    }

    /**
     * Maneja las acciones cuando un socio está golpeado y matado.
     */
    public void allyHit() {
        resManager.playSound(SOUND_EXPLOSION);
        resManager.playSound(SOUND_SHIRLEY_DEATH_NOO);
        resManager.playSound(SOUND_FALLING_SHEEP_DEATH);
    }

    /**
     * Maneja las acciones cuando un inimigo está matado por una bomba.
     */
    public void handleEnemyHitByBomb() {
        resManager.playSound(SOUND_EXPLOSION);
        resManager.playSound(SOUND_SHAUN_CELEBRATION);
        resManager.playSound(SOUND_ENEMY_DEATH);
    }

    /**
     * Maneja las acciones cuando un jugado está matado.
     * @param delay
     */
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

    /**
     * Método asíncrono que programar el método para re-empezar el estado del jugador depues de morir.
     * @param delay
     */
    public void schedulePlayerRestart(int delay) {
        Timer.Task task = new Timer.Task() {
            @Override
            public void run() {
                restartPlayer();
            }
        };
        Timer.instance().scheduleTask(task, delay);
    }

    /**
     * Re-empezar al jugador.
     */
    private void restartPlayer() {
        if (hud.getEnergy() <= 0) {
            hud.updateEnergy(4);
        }
        resetPlayer();
        hud.resetWorldTimer();
    }

    /**
     * Re-establece al jugador.
     */
    public void resetPlayer() {
        if (!b2WorldManager.getWorld().isLocked() && player.isHasLostLife()) {
            b2WorldManager.getWorld().destroyBody(player.getB2Body());
            player = null;
            player = new Shaun(resManager.loadRegion("shaun_idle", -1), b2WorldManager.getWorld(), 32, 38, 8, this);
        }
    }

    /**
     * Maneja las acciones cuando una oveja está matado.
     */
    public void handleSheepDeath() {
        resManager.playSound(SOUND_SHIRLEY_DEATH_NOO);
        resManager.playSound(SOUND_SHAUN_DEATH_NOO);
        resManager.playSound(SOUND_EXPLOSION);
        hud.updateScore(POINTS_SHEEP_DEATH);
    }

    /**
     * Maneja las acciones cuando una oveja atrapada está salvado.
     */
    public void handleSavedSheep() {
        resManager.playSound(SOUND_SHIRLEY_CELEBRATION);
        resManager.playSound(SOUND_SHAUN_CELEBRATION);
        hud.updateScore(POINTS_SHEEP_SAVED);
    }

    /**
     * Maneja las acciones cuando un nivel está terminado con éxito.
     */
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

    /**
     * Maneja el sonido de una oveja cayendose.
     */
    public void handleFallingSheep() {
        resManager.playSound(SOUND_TIMMY_TRAMPOLINE);
    }

    /**
     * Maneja las acciones cuando el jugador está paralizado por un KamikazeSheep.
     */
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
