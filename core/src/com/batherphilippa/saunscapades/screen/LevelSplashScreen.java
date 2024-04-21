package com.batherphilippa.saunscapades.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.batherphilippa.saunscapades.SaunScapades;
import com.batherphilippa.saunscapades.manager.OptionManager;
import com.batherphilippa.saunscapades.screen.util.UIUtils;
import com.kotcrab.vis.ui.widget.*;

import static com.batherphilippa.saunscapades.screen.constants.UIConstants.*;

public class LevelSplashScreen implements Screen {

    private final SaunScapades game;
    private final int score;
    private Stage stage;
    private VisTextButton exitBtn;
    private VisTextButton playBtn;
    private VisTextButton saveBtn;
    private VisLabel missionTitle;
    private VisLabel nextLevelAvoid;
    private VisLabel nextLevelInfo;
    private VisLabel scoreInfo;
    private VisLabel title;

    public LevelSplashScreen(SaunScapades game) {
        this.game = game;
        this.score = SaunScapades.score;
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
        this.nextLevelInfo = new VisLabel(TEXT_AREA_LEVEL_2_INFO);
        this.nextLevelAvoid = new VisLabel(TEXT_AREA_LEVEL_2_AVOID);
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
        table.add(nextLevelInfo).center().height(50).pad(5);
        table.row();
        table.add(nextLevelAvoid).center().height(50).pad(5);

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
        SaunScapades.setGameState(GameState.PAUSED);
    }

    @Override
    public void resume() {
        SaunScapades.setGameState(GameState.RUNNING);
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
