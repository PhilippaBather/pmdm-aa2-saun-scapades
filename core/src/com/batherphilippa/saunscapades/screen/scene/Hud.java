package com.batherphilippa.saunscapades.screen.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Disposable;
import com.batherphilippa.saunscapades.ShaunScapades;
import com.batherphilippa.saunscapades.manager.CameraManager;
import com.batherphilippa.saunscapades.manager.ConfigManager;
import com.batherphilippa.saunscapades.screen.GameState;
import com.batherphilippa.saunscapades.screen.util.UIUtils;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

/**
 * Hud - define el HUD que contiene información sobre la punctuación, energía, nivel actual, vidas y el temporazidor.
 */
public class Hud implements Disposable {

    private final SpriteBatch batch;

    // tabla de HUD
    private final Stage stage;
    private VisLabel energyLabel;
    private VisLabel energyValueLabel;
    private VisLabel levelLabel;
    private VisLabel levelValueLabel;
    private VisLabel livesLabel;
    private VisLabel livesValueLabel;
    private VisLabel scoreLabel;
    private VisLabel scoreValueLabel;
    private VisLabel timerLabel;
    private VisLabel timerValueLabel;

    // nivel
    private int level;

    // puntos, energía y vidas
    private int energy;
    private int lives;
    private int score;

    // temporizador
    private float timeCount;
    private int worldTimer;

    private boolean isTimerStopped;

    public Hud(SpriteBatch batch, CameraManager camManager) {
        this.batch = batch;

        setInitialValues();

        VisTable table = UIUtils.createTableObj();
        defineTable(table);

        this.stage = new Stage(camManager.getHudPort(), batch);
        this.stage.addActor(table);
    }

    public void setInitialValues() {
        this.energy = (int) ConfigManager.getGameEnergy();
        this.level = ShaunScapades.currGameLevel.getLevel();
        this.lives = (int) ConfigManager.getGameLives();
        this.score = ShaunScapades.score;
        this.timeCount = 0;
        this.worldTimer = (int) ConfigManager.getGameLength();
    }

    private void defineTable(VisTable table) {
        table.top();
        table.setFillParent(true);  // tabla está el tamaño de la escena
        setLabels();
        table.add(livesLabel).expandX().pad(2);
        table.add(energyLabel).expandX().pad(2);
        table.add(levelLabel).expandX().pad(2);
        table.add(scoreLabel).expandX().pad(2);
        table.add(timerLabel).expandX().pad(2);
        table.row().height(0.5f);
        table.add(livesValueLabel).expandX();
        table.add(energyValueLabel).expandX();
        table.add(levelValueLabel).expandX();
        table.add(scoreValueLabel).expandX();
        table.add(timerValueLabel).expandX();
    }

    private void setLabels() {
        energyLabel = new VisLabel("Energy", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        energyValueLabel = new VisLabel(String.format("%01d", energy), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new VisLabel("GameLevel", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelValueLabel = new VisLabel(String.format("%01d", level), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        livesLabel = new VisLabel("Lives", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        livesValueLabel = new VisLabel(String.format("%02d", lives), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new VisLabel("Points", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreValueLabel = new VisLabel(String.format("%04d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timerLabel = new VisLabel("Time", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timerValueLabel = new VisLabel(String.format(String.format("%04d", worldTimer)), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    }

    public int getScore() {
        return score;
    }

    public int getEnergy() {
        return energy;
    }

    public int getWorldTimer() {
        return worldTimer;
    }

    public int getLives() {
        return lives;
    }

    public void updateEnergy(int update) {
        this.energy += update;
        energyValueLabel.setText(String.format("%02d", energy));
    }

    public void updateLives(int update) {
        lives += update;
        livesValueLabel.setText(String.format("%02d", lives));
        if (lives <= 0) {
            ShaunScapades.setGameState(GameState.GAME_OVER);
        }
    }

    public void updateScore(int value) {
        score += value;
        scoreValueLabel.setText(String.format("%04d", score));

    }

    public void updateTimer(float dt) {
        if (!isTimerStopped) {
            timeCount += dt;
            if (timeCount >= 1) { // 1 segundo
                worldTimer--;
                timerValueLabel.setText(String.format("%04d", worldTimer));
                timeCount = 0;
            }
        }
    }

    public void resetWorldTimer() {
        worldTimer = 181;
        isTimerStopped = false;
    }

    public void stopTimer() {
        isTimerStopped = true;
    }

    public void draw() {
        stage.draw();
        stage.act();
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
    }
}
