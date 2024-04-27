package com.batherphilippa.saunscapades.screen.scene;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.batherphilippa.saunscapades.ShaunScapades;
import com.batherphilippa.saunscapades.manager.OptionManager;
import com.batherphilippa.saunscapades.screen.util.UIUtils;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import static com.batherphilippa.saunscapades.screen.constants.UIConstants.BTN_RESUME;
import static com.batherphilippa.saunscapades.screen.constants.UIConstants.LABEL_GAME_PAUSED_TITLE;

/**
 * PauseBackground - define una pantalla de fondo cuando el juego está en el estado de pausa; implementa Disposable.
 */
public class PauseBackground implements Disposable {

    private final ShaunScapades game;
    private final Stage stage;
    private final VisTextButton resumeBtn;

    public PauseBackground(ShaunScapades game) {
        this.game = game;

        VisTable table = UIUtils.createTableObj();
        resumeBtn = new VisTextButton(BTN_RESUME);
        OptionManager.handleUnPauseClicked(resumeBtn);
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
        table.add(resumeBtn).expandX().pad(10);
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
