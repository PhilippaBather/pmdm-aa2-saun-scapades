package com.batherphilippa.saunscapades.screen.scene;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.batherphilippa.saunscapades.SaunScapades;
import com.batherphilippa.saunscapades.manager.OptionManager;
import com.batherphilippa.saunscapades.screen.util.UIUtils;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import static com.batherphilippa.saunscapades.screen.constants.UIConstants.BTN_RESUME;
import static com.batherphilippa.saunscapades.screen.constants.UIConstants.LABEL_GAME_PAUSED_TITLE;

public class PauseBackground implements Disposable {

    private final SaunScapades game;
    private final Stage stage;
    private final VisTextButton resumBtn;

    public PauseBackground(SaunScapades game) {
        this.game = game;

        VisTable table = UIUtils.createTableObj();
        resumBtn = new VisTextButton(BTN_RESUME);
        OptionManager.handleUnPauseClicked(resumBtn);
        defineTable(table);

        this.stage = new Stage();
        this.stage.addActor(table);
    }

    public Stage getStage() {
        return stage;
    }

    private void defineTable(VisTable table) {
        table.center();
        table.setFillParent(true);
        table.add(LABEL_GAME_PAUSED_TITLE).expandX().pad(10);
        table.row();
        table.add(resumBtn).expandX().pad(10);
    }

    public void draw() {
        stage.draw();
        stage.act();
    }

    @Override
    public void dispose() {
        game.dispose();
        stage.dispose();
    }
}
