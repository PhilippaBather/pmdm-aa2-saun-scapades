package com.batherphilippa.saunscapades.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.batherphilippa.saunscapades.ShaunScapades;
import com.batherphilippa.saunscapades.manager.OptionManager;
import com.batherphilippa.saunscapades.manager.ResourceManager;
import com.batherphilippa.saunscapades.screen.util.UIUtils;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import static com.batherphilippa.saunscapades.screen.constants.UIConstants.*;

public class GameOverScreen implements Screen {

    private final ShaunScapades game;
    private final ResourceManager resManager;
    private Stage stage;
    private VisLabel titleLabel;
    private VisTextButton exitBtn;
    private VisTextButton mainMenuBtn;
    private VisTextButton playBtn;

    private final int score;

    public GameOverScreen(ShaunScapades game) {
        this.game = game;
        this.resManager = this.game.getResManager();

        this.score = ShaunScapades.score;
    }

    @Override
    public void show() {
        UIUtils.clearScreen();

        VisTable infoTable = UIUtils.createTableObj();
        VisTable actionsTable = UIUtils.createTableObj();

        stage = new Stage();
        stage.addActor(infoTable);
        stage.addActor(actionsTable);

        createComponents();
        setClickListeners();
        createTableStructure(infoTable, actionsTable);

        Gdx.input.setInputProcessor(stage);

        resManager.playSound("game_over");
    }

    private void createComponents() {
        this.titleLabel = new VisLabel(LABEL_GAME_OVER_TITLE);
        this.exitBtn = new VisTextButton(BTN_EXIT);
        this.mainMenuBtn = new VisTextButton(BTN_MAIN_MENU);
        this.playBtn = new VisTextButton(BTN_PLAY_AGAIN);
    }

    private void setClickListeners() {
        OptionManager.handleExitClicked(exitBtn, this);
        OptionManager.handleMainMenuClicked(mainMenuBtn, game, this); // screen game, menu type, sprite manager needed as param
        OptionManager.handlePlayClicked(playBtn, game, this);
    }

    private void createTableStructure(VisTable infoTable, VisTable actionsTable) {
        // info table
        infoTable.setPosition(0, 70);
        infoTable.row();
        infoTable.add(titleLabel).center().width(50).height(40).pad(5);
        infoTable.row();
        infoTable.add("Points: " + score).center().width(50).height(40).pad(5);
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
        ShaunScapades.gameState = GameState.RUNNING;
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
