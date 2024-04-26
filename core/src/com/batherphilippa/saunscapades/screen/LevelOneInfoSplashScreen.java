package com.batherphilippa.saunscapades.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.batherphilippa.saunscapades.ShaunScapades;
import com.batherphilippa.saunscapades.screen.util.UIUtils;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import static com.batherphilippa.saunscapades.screen.constants.UIConstants.*;

public class LevelOneInfoSplashScreen implements Screen {

    private final ShaunScapades game;
    private Stage stage;
    private boolean splashDone;
    private VisLabel titleLabel;
    private VisLabel infoLabel1;
    private VisLabel infoLabel2;
    private VisLabel infoLabel3;
    private VisLabel infoLabel4;
    private VisLabel infoLabel5;

    public LevelOneInfoSplashScreen(ShaunScapades game) {
        this.game = game;
        this.splashDone = false;

   }

    @Override
    public void show() {
        UIUtils.clearScreen();

        VisTable table = UIUtils.createTableObj();

        stage = new Stage();
        stage.addActor(table);

        createComponents();
        createTableStructure(table);

        stage.addAction(Actions.sequence(Actions.alpha(0),
                Actions.fadeIn(1.5f),
                Actions.delay(15f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        splashDone = true;
                    }
                })));
    }

    private void createComponents() {
        this.titleLabel = new VisLabel(LABEL_LEVEL_1_INFO_TITLE);
        this.infoLabel1 = new VisLabel(LABEL_LEVEL_1_INFO_1);
        this.infoLabel2 = new VisLabel(LABEL_LEVEL_1_INFO_2);
        this.infoLabel3 = new VisLabel(LABEL_LEVEL_1_INFO_3);
        this.infoLabel4 = new VisLabel(LABEL_LEVEL_1_INFO_4);
        this.infoLabel5 = new VisLabel(LABEL_LEVEL_1_INFO_5);
    }

    private void createTableStructure(VisTable table) {
        table.setFillParent(true);
        table.setPosition(0, 50);
        table.row();
        table.add(titleLabel).center().height(40).pad(5);
        table.row();
        table.addSeparator();
        table.row();
        table.add(infoLabel1).center().height(50).pad(5);
        table.row();
        table.add(infoLabel2).center().height(50).pad(5);
        table.row();
        table.add(infoLabel3).center().height(50).pad(5);
        table.row();
        table.add(infoLabel4).center().height(50).pad(5);
        table.row();
        table.add(infoLabel5).center().height(50).pad(5);
    }

    @Override
    public void render(float delta) {
        UIUtils.clearScreen();

        stage.act(delta);
        stage.draw();

        if (splashDone) {
            dispose();
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        ShaunScapades.setGameState(GameState.PAUSED);
    }

    @Override
    public void resume() {
        ShaunScapades.setGameState(GameState.RUNNING);
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
