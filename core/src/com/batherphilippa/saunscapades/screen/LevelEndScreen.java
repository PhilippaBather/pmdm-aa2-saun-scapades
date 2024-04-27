package com.batherphilippa.saunscapades.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.batherphilippa.saunscapades.ShaunScapades;
import com.batherphilippa.saunscapades.manager.OptionManager;
import com.batherphilippa.saunscapades.screen.util.UIUtils;
import com.kotcrab.vis.ui.widget.*;

import static com.batherphilippa.saunscapades.screen.constants.UIConstants.*;

public class LevelEndScreen implements Screen {

    private final ShaunScapades game;
    private final int score;
    private Stage stage;
    private VisTextButton exitBtn;
    private VisTextButton playBtn;
    private VisTextButton saveBtn;
    private VisLabel missionTitle;
    private VisLabel nextLevelInfo1;
    private VisLabel nextLevelInfo2;
    private VisLabel nextLevelInfo3;
    private VisLabel nextLevelInfo4;
    private VisLabel scoreInfo;
    private VisLabel title;

    public LevelEndScreen(ShaunScapades game) {
        this.game = game;
        this.score = ShaunScapades.score;
    }

    @Override
    public void show() {
        UIUtils.clearScreen();

        VisTable infoTable = UIUtils.createTableObj();
        VisTable actionTable = UIUtils.createTableObj();
        stage = new Stage();
        stage.addActor(infoTable);
        stage.addActor(actionTable);

        createComponents();
        setClickListeners();
        createTableStructure(infoTable, actionTable);

        Gdx.input.setInputProcessor(stage);
    }

    private void createComponents() {
        this.title = new VisLabel(TEXT_FIELD_LEVEL_END);
        this.scoreInfo = new VisLabel(String.format(TEXT_FIELD_SCORE, score));
        this.missionTitle = new VisLabel(LABEL_MISSION_2);
        this.nextLevelInfo1 = new VisLabel(LABEL_LEVEL_2_INFO_1);
        this.nextLevelInfo2 = new VisLabel(LABEL_LEVEL_2_INFO_2);
        this.nextLevelInfo3 = new VisLabel(LABEL_LEVEL_2_INFO_3);
        this.nextLevelInfo4 = new VisLabel(LABEL_LEVEL_2_INFO_4);
        this.playBtn = new VisTextButton(BTN_PLAY);
        this.saveBtn = new VisTextButton(BTN_SAVE);
        this.exitBtn = new VisTextButton(BTN_EXIT);
    }

    private void setClickListeners() {
        OptionManager.handlePlayNewLevelClicked(playBtn, game, this);
        OptionManager.handleSaveClicked(saveBtn, game, this);
        OptionManager.handleExitClicked(exitBtn, this);
    }

    private void createTableStructure(VisTable table, VisTable actionsTable) {
        table.setFillParent(true);
        table.setPosition(0, 50);
        table.row();
        table.add(title).center().height(40).pad(5);
        table.row();
        table.addSeparator();
        table.row();
        table.add(scoreInfo).center().height(40).pad(5);
        table.row();
        table.addSeparator();
        table.row();
        table.add(missionTitle).center().height(40).pad(5);
        table.row();
        table.add(nextLevelInfo1).center().height(50).pad(5);
        table.row();
        table.add(nextLevelInfo2).center().height(50).pad(5);
        table.row();
        table.add(nextLevelInfo3).center().height(50).pad(5);
        table.row();
        table.add(nextLevelInfo4).center().height(50).pad(5);

        // action button table
        actionsTable.setPosition(0, -200);
        actionsTable.row();
        actionsTable.add(playBtn).center().width(150).height(30).pad(5);
        actionsTable.add(saveBtn).center().width(150).height(30).pad(5);
        actionsTable.add(exitBtn).center().width(150).height(30).pad(5);
    }

    @Override
    public void render(float delta) {
        UIUtils.clearScreen();

        stage.act(delta);
        stage.draw();

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
