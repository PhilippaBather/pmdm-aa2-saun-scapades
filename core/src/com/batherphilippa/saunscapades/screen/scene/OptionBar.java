package com.batherphilippa.saunscapades.screen.scene;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.batherphilippa.saunscapades.ShaunScapades;
import com.batherphilippa.saunscapades.manager.CameraManager;
import com.batherphilippa.saunscapades.manager.OptionManager;
import com.batherphilippa.saunscapades.screen.util.UIUtils;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import static com.batherphilippa.saunscapades.screen.constants.UIConstants.*;

public class OptionBar implements Disposable {

    private final ShaunScapades game;
    private final SpriteBatch batch;
    private final Stage stage;

    // opciones de la tabla
    private VisTextButton menuBtn;
    private VisTextButton pauseBtn;
    private VisTextButton exitBtn;

    public OptionBar(ShaunScapades game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
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
        table.add(pauseBtn).height(15);
        table.add(menuBtn).height(15);
        table.add(exitBtn).height(15);
    }

    private void setTextButtons() {
        menuBtn = new VisTextButton(BTN_MENU);
        pauseBtn = new VisTextButton(BTN_PAUSE);
        exitBtn = new VisTextButton(BTN_EXIT);
    }

    private void addClickListeners() {
        OptionManager.handlePauseClicked(pauseBtn);
        OptionManager.handleExitClicked(exitBtn, null);
        OptionManager.handleMainMenuClicked(menuBtn, game, null);
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
