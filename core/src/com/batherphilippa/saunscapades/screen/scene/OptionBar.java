package com.batherphilippa.saunscapades.screen.scene;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.batherphilippa.saunscapades.ShaunScapades;
import com.batherphilippa.saunscapades.manager.CameraManager;
import com.batherphilippa.saunscapades.manager.OptionManager;
import com.batherphilippa.saunscapades.screen.GameScreen;
import com.batherphilippa.saunscapades.screen.util.UIUtils;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import static com.batherphilippa.saunscapades.screen.constants.UIConstants.*;

/**
 * OptionBar - define la barra de opciones en la pantalla de juego: pause ('pausa'), main menu ('menú principal`) y
 * exit.
 */
public class OptionBar implements Disposable {

    private final ShaunScapades game;
    private final SpriteBatch batch;
    private final GameScreen gameScreen;
    private final Stage stage;

    // opciones de la tabla
    private VisTextButton gameMenuBtn;
    private VisTextButton exitBtn;

    public OptionBar(ShaunScapades game, SpriteBatch batch, GameScreen gameScreen) {
        this.game = game;
        this.batch = batch;
        this.gameScreen = gameScreen;
        CameraManager camManager = this.game.getCamManager();

        VisTable table = UIUtils.createTableObj();
        setTextButtons();
        addClickListeners();
        defineTable(table);

        this.stage = new Stage(camManager.getOptionPort(), batch);
        this.stage.addActor(table);

    }

    private void defineTable(VisTable table) {
        table.bottom().left();
        table.add(gameMenuBtn).center().height(10).width(50);
        table.add(exitBtn).center().height(10).width(50);
    }

    private void setTextButtons() {
        gameMenuBtn = new VisTextButton(BTN_GAME_MENU);
        exitBtn = new VisTextButton(BTN_EXIT);
    }

    private void addClickListeners() {
        OptionManager.handlePauseClicked(gameMenuBtn);
        OptionManager.handleExitClicked(exitBtn, gameScreen);
    }

    public Stage getStage() {
        return this.stage;
    }

    public void draw(float dt) {
        stage.act(dt);
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        game.dispose();
        stage.dispose();
    }
}
