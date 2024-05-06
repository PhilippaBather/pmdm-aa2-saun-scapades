package com.batherphilippa.saunscapades.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.batherphilippa.saunscapades.ShaunScapades;
import com.batherphilippa.saunscapades.manager.ConfigManager;
import com.batherphilippa.saunscapades.manager.OptionManager;
import com.batherphilippa.saunscapades.screen.util.UIUtils;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisList;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import static com.batherphilippa.saunscapades.screen.constants.ConfigConstants.*;
import static com.batherphilippa.saunscapades.screen.constants.UIConstants.*;

/**
 * ConfigScreen - define la pantalla de configuración; implementa Screen.
 */
public class ConfigScreen implements Screen {

    private final ShaunScapades game;
    private Stage stage;
    private VisLabel gameDiffLab;
    private VisLabel gameLengthLab;
    private VisLabel optionsMenuLab;
    private VisList<String> gameDiffList;
    private VisList<String> gameLengthList;
    private VisTextButton exitBtn;
    private VisTextButton mainMenuBtn;
    private VisTextButton playBtn;

    public ConfigScreen(ShaunScapades game) {
        this.game = game;
    }

    @Override
    public void show() {
        UIUtils.clearScreen();

        VisTable optionsTable = UIUtils.createTableObj();
        VisTable actionsTable = UIUtils.createTableObj();

        stage = new Stage();
        stage.addActor(optionsTable);
        stage.addActor(actionsTable);

        createComponents();
        setPreferences();
        setClickListeners();
        createTableStructure(optionsTable, actionsTable);

        Gdx.input.setInputProcessor(stage);
    }

    private void createComponents() {
        exitBtn = new VisTextButton(BTN_EXIT);
        mainMenuBtn = new VisTextButton(BTN_MAIN_MENU);
        playBtn = new VisTextButton(BTN_PLAY);
        gameDiffLab = new VisLabel(LABEL_GAME_DIFFICULTY);
        gameLengthLab = new VisLabel(LABEL_GAME_LENGTH);
        optionsMenuLab = new VisLabel(LABEL_OPTIONS_MENU_TITLE);
        String[] timeLengths = {GAME_LENGTH_STR_SHORT, GAME_LENGTH_STR_MEDIUM, GAME_LENGTH_STR_LONG};
        gameLengthList = new VisList<>();
        gameLengthList.setItems(timeLengths);
        String[] diffOptions = {GAME_EASY, GAME_MEDIUM, GAME_HARD};
        gameDiffList = new VisList<>();
        gameDiffList.setItems(diffOptions);
    }

    private void setPreferences() {
        gameLengthList.setSelected(ConfigManager.getGameLengthOption());
        gameDiffList.setSelected(ConfigManager.getGameDiffOption());
    }

    private void setClickListeners() {
        ConfigManager.handleGameLengthPref(gameLengthList);
        ConfigManager.handleGameDifficultyPref(gameDiffList);
        OptionManager.handleExitClicked(exitBtn, this);
        OptionManager.handlePlayClicked(playBtn, game, this);
        OptionManager.handleMainMenuClicked(mainMenuBtn, game, this);
    }

    private void createTableStructure(VisTable optionsTable, VisTable actionsTable) {
        // menu table
        optionsTable.setPosition(0, 50);
        optionsTable.row();
        optionsTable.add(optionsMenuLab).center().width(250).height(40).pad(5);
        optionsTable.row();
        optionsTable.addSeparator();
        optionsTable.row();
        optionsTable.add(gameLengthLab).center().width(150).height(40).pad(5);
        optionsTable.row().center();
        optionsTable.add(gameLengthList).center().width(150).height(75).pad(5);
        optionsTable.row();
        optionsTable.add(gameDiffLab).center().width(150).height(40).pad(5);
        optionsTable.row().center();
        optionsTable.add(gameDiffList).center().width(150).height(75).pad(5);

        // action button table
        actionsTable.setPosition(0, -200);
        actionsTable.row();
        actionsTable.add(playBtn).center().width(150).height(30).pad(5);
        actionsTable.add(mainMenuBtn).center().width(150).height(30).pad(5);
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
        ShaunScapades.gameState = GameState.PAUSED;
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
