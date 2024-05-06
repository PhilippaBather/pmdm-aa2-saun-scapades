package com.batherphilippa.saunscapades.screen.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.batherphilippa.saunscapades.ShaunScapades;
import com.batherphilippa.saunscapades.manager.ConfigManager;
import com.batherphilippa.saunscapades.manager.OptionManager;
import com.batherphilippa.saunscapades.screen.GameScreen;
import com.batherphilippa.saunscapades.screen.util.UIUtils;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisList;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import static com.batherphilippa.saunscapades.screen.constants.ConfigConstants.SOUND_OFF;
import static com.batherphilippa.saunscapades.screen.constants.ConfigConstants.SOUND_ON;
import static com.batherphilippa.saunscapades.screen.constants.UIConstants.*;

/**
 * PauseBackground - define una pantalla de fondo cuando el juego está en el estado de pausa; implementa Disposable.
 */
public class PauseBackground implements Disposable {

    private final ShaunScapades game;
    private final GameScreen gameScreen;
    private final Stage stage;
    private VisLabel titleLbl;
    private VisLabel soundSelectionLbl;
    private VisList<String> soundOptionsList;
    private VisTextButton resumeBtn;
    private VisTextButton mainMenuBtn;
    private VisTextButton exitBtn;

    public PauseBackground(ShaunScapades game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;

        VisTable actionsTable = UIUtils.createTableObj();
        VisTable optionsTable = UIUtils.createTableObj();

        createComponents();
        setClickListeners();
        setSoundPreference();

        defineTable(actionsTable, optionsTable);

        this.stage = new Stage();
        this.stage.addActor(optionsTable);
        this.stage.addActor(actionsTable);

        Gdx.input.setInputProcessor(stage);
    }

    private void createComponents() {
        this.exitBtn = new VisTextButton(BTN_EXIT);
        this.mainMenuBtn = new VisTextButton(BTN_MAIN_MENU);
        this.resumeBtn = new VisTextButton(BTN_RESUME);
        this.soundSelectionLbl = new VisLabel(LABEL_SOUND_ON_OFF);
        String[] soundOptions = {SOUND_ON, SOUND_OFF};
        soundOptionsList = new VisList<>();
        soundOptionsList.setItems(soundOptions);
        this.titleLbl = new VisLabel(LABEL_GAME_SCREEN_MENU);
    }

    private void setClickListeners() {
        ConfigManager.handleGameSoundPref(soundOptionsList);
        OptionManager.handleUnPauseClicked(resumeBtn);
        OptionManager.handleExitClicked(exitBtn, gameScreen);
        OptionManager.handleMainMenuClicked(mainMenuBtn, game, gameScreen);
    }

    private void setSoundPreference() {
        soundOptionsList.setSelected(ConfigManager.getGameLengthOption());
    }

    public Stage getStage() {
        return stage;
    }

    private void defineTable(VisTable actionsTable, VisTable optionsTable) {

        optionsTable.setFillParent(true);
        optionsTable.setPosition(0, 50);
        optionsTable.row();
        optionsTable.add(titleLbl);
        optionsTable.row();
        optionsTable.addSeparator();
        optionsTable.row();
        optionsTable.add(soundSelectionLbl).center().width(150).height(40).pad(5);
        optionsTable.row().center();
        optionsTable.add(soundOptionsList).center().width(150).height(75).pad(5);

        actionsTable.setPosition(0, -200);
        actionsTable.row();
        actionsTable.row();
        actionsTable.add(mainMenuBtn).center().width(150).height(30).pad(5);
        actionsTable.add(resumeBtn).center().width(150).height(30).pad(5);
        actionsTable.add(exitBtn).center().width(150).height(30).pad(5);
    }

    public void draw() {
        stage.draw();
        stage.act();
    }

    @Override
    public void dispose() {
        game.dispose();
        gameScreen.dispose();
        stage.dispose();
    }
}
