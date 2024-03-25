package com.batherphilippa.saunscapades.scene;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.batherphilippa.saunscapades.SaunScapades;
import com.batherphilippa.saunscapades.scene.util.UIUtils;
import com.kotcrab.vis.ui.widget.VisTable;

public class PauseBackground implements Disposable {

    private SaunScapades game;
    private final Stage stage;
    public PauseBackground(SaunScapades game) {
        this.game = game;

        VisTable table = UIUtils.createTableObj();
        defineTable(table);

        this.stage = new Stage();
        this.stage.addActor(table);
    }

    private void defineTable(VisTable table) {
        table.center();
        table.setFillParent(true);
        table.add("Pause").expandX().pad(10);
        table.add("Press Down Arrow to Return").expandX().pad(10);
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
