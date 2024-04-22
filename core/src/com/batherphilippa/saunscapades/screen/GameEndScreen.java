package com.batherphilippa.saunscapades.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.batherphilippa.saunscapades.SaunScapades;
import com.batherphilippa.saunscapades.manager.OptionManager;
import com.batherphilippa.saunscapades.manager.ResourceManager;
import com.batherphilippa.saunscapades.screen.util.UIUtils;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import static com.batherphilippa.saunscapades.manager.constants.ResourcesConstants.MUSIC_GAME_FINISHED;
import static com.batherphilippa.saunscapades.screen.constants.UIConstants.*;

public class GameEndScreen implements Screen {
    private final SaunScapades game;
    private final ResourceManager resManager;
    private final int score;
    private Stage stage;
    private VisLabel title;
    private VisLabel scoreInfo;
    private VisTextButton exitBtn;
    private VisTextButton menuBtn;
    private VisTextButton playBtn;
    private VisTextButton saveBtn;

    public GameEndScreen(SaunScapades game) {
        this.game = game;
        this.resManager = this.game.getResManager();
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

        resManager.playMusic(MUSIC_GAME_FINISHED, false);

        Gdx.input.setInputProcessor(stage);
    }

    private void createComponents() {
        this.title = new VisLabel(LABEL_GAME_FINISHED);
        this.scoreInfo = new VisLabel(String.format(TEXT_FIELD_SCORE, score));
        this.exitBtn = new VisTextButton(BTN_EXIT);
        this.menuBtn = new VisTextButton(BTN_MAIN_MENU);
        this.playBtn = new VisTextButton(BTN_PLAY);
        this.saveBtn = new VisTextButton(BTN_SAVE);
    }

    private void setClickListeners() {
        OptionManager.handleExitClicked(exitBtn, this);
        OptionManager.handleMainMenuClicked(menuBtn, game, this);
        OptionManager.handlePlayClicked(playBtn, game, this);
        OptionManager.handleSaveClicked(saveBtn, game, this);
    }

    private void createTableStructure(VisTable infoTable, VisTable actionsTable) {
        // info table
        infoTable.setFillParent(true);
        infoTable.setPosition(0, 20);
        infoTable.row();
        infoTable.add(title).center().height(40).pad(5);
        infoTable.row();
        infoTable.addSeparator();
        infoTable.row();
        infoTable.add(scoreInfo).center().height(40).pad(5);
        infoTable.row();
        infoTable.addSeparator();

        // actions table
        actionsTable.setPosition(0, -175);
        actionsTable.row();
        actionsTable.add(playBtn).center().width(125).height(30).pad(5);
        actionsTable.add(saveBtn).center().width(125).height(30).pad(5);
        actionsTable.add(menuBtn).center().width(125).height(30).pad(5);
        actionsTable.add(exitBtn).center().width(125).height(30).pad(5);
    }

    @Override
    public void render(float delta) {
        UIUtils.clearScreen();

        stage.act();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
