package com.batherphilippa.saunscapades.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.batherphilippa.saunscapades.SaunScapades;
import com.batherphilippa.saunscapades.manager.OptionManager;
import com.batherphilippa.saunscapades.screen.util.UIUtils;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class MainMenu implements Screen {

    private final SaunScapades game;
    private Stage stage;
    private VisLabel titleLabel;
    private VisTextButton configBtn;
    private VisTextButton exitBtn;
    private VisTextButton playBtn;

    public MainMenu(SaunScapades game) {
        this.game = game;
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
    }

    private void createComponents() {
        this.titleLabel = new VisLabel("MAIN MENU");
        this.exitBtn = new VisTextButton("EXIT");
        this.configBtn = new VisTextButton("CONFIGURATIONS");
        this.playBtn = new VisTextButton("PLAY AGAIN");
    }

    private void setClickListeners() {
        OptionManager.handleExitClicked(exitBtn, this);
//        OptionManager.handleConfigurationClicked(configBtn, game, this); // screen game, menu type, sprite manager needed as param
        OptionManager.handlePlayAgainClicked(playBtn, game, this);
    }

    private void createTableStructure(VisTable infoTable, VisTable actionsTable) {
        // info table
        infoTable.setPosition(0, 70);
        infoTable.row();
        infoTable.add(titleLabel).center().width(50).height(40).pad(5);
        // action button table
        actionsTable.setPosition(0, -200);
        actionsTable.row();
        actionsTable.add(playBtn).center().width(150).height(30).pad(5);
        actionsTable.add(configBtn).center().width(150).height(30).pad(5);
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
        SaunScapades.gameState = GameState.PAUSED;
    }

    @Override
    public void resume() {
        SaunScapades.gameState = GameState.RUNNING;
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.stage.dispose();
    }
}
