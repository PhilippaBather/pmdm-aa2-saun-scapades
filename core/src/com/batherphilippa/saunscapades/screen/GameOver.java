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

public class GameOver implements Screen {

    private final SaunScapades game;
    private Stage stage;
    private VisLabel titleLabel;
    private VisTextButton exitBtn;
    private VisTextButton mainMenuBtn;
    private VisTextButton playBtn;

    public GameOver(SaunScapades game) {
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
        this.titleLabel = new VisLabel("GAME OVER!!!");
        this.exitBtn = new VisTextButton("EXIT");
        this.mainMenuBtn = new VisTextButton("MAIN MENU");
        this.playBtn = new VisTextButton("PLAY AGAIN");
    }

    private void setClickListeners() {
        OptionManager.handleExitClicked(exitBtn, this);
        OptionManager.handleMainMenuClicked(mainMenuBtn, game, this); // screen game, menu type, sprite manager needed as param
        OptionManager.handlePlayAgainClicked(playBtn, game, this);
    }

    private void createTableStructure(VisTable infoTable, VisTable actionsTable) {
        // info table
        infoTable.setPosition(0, 70);
        infoTable.row();
        infoTable.add(titleLabel).center().width(50).height(40).pad(5);
        infoTable.row();
        infoTable.add("Points: " + game.getTotalPoints()).center().width(50).height(40).pad(5);
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
        game.gameState = GameState.PAUSED;
    }

    @Override
    public void resume() {
        game.gameState = GameState.RUNNING;
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
