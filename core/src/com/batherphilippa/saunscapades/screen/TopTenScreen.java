package com.batherphilippa.saunscapades.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.batherphilippa.saunscapades.ShaunScapades;
import com.batherphilippa.saunscapades.domain.player.Player;
import com.batherphilippa.saunscapades.manager.OptionManager;
import com.batherphilippa.saunscapades.screen.util.UIUtils;
import com.kotcrab.vis.ui.widget.*;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Comparator;
import java.util.List;

import static com.batherphilippa.saunscapades.screen.constants.AppConstants.CSV_PATH;
import static com.batherphilippa.saunscapades.screen.constants.UIConstants.*;

/**
 * Pantalla para mostrar la puntuación (nombres y puntos) del 'top ten'.
 */
public class TopTenScreen implements Screen {

    private final ShaunScapades game;
    private List<Player> topTenList;
    private Stage stage;
    private VisLabel titleLabel;
    private VisTextButton mainMenuBtn;
    private VisTextButton exitBtn;

    public TopTenScreen(ShaunScapades game) {
        this.game = game;
        this.topTenList = readCSVFile();
        if (this.topTenList != null) {
            topTenList.sort(Comparator.comparingInt(Player::getPlayerScore).reversed());
        }
    }

    @Override
    public void show() {

        UIUtils.clearScreen();

        VisTable topTenTable = UIUtils.createTableObj();
        VisTable actionsTable = UIUtils.createTableObj();

        stage = new Stage();
        stage.addActor(topTenTable);
        stage.addActor(actionsTable);

        createComponents();
        setClickListeners();
        createTableStructure(topTenTable, actionsTable);

        Gdx.input.setInputProcessor(stage);
    }

    private List<Player> readCSVFile() {
        String filename = CSV_PATH;
        try {
            return new CsvToBeanBuilder(new FileReader(filename))
                    .withType(Player.class)
                    .build()
                    .parse();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createComponents() {
        this.titleLabel = new VisLabel(LABEL_TOP_TEN_SCORES);
        this.mainMenuBtn = new VisTextButton(BTN_MAIN_MENU);
        this.exitBtn = new VisTextButton(BTN_EXIT);
    }

    private void setClickListeners() {
        OptionManager.handleMainMenuClicked(mainMenuBtn, game, this);
        OptionManager.handleExitClicked(exitBtn, this);
    }

    private void createTableStructure(VisTable topTenTable, VisTable actionsTable) {
        topTenTable.setFillParent(true);
        topTenTable.setPosition(0, 20);
        topTenTable.row();
        topTenTable.add(titleLabel).center().height(40).pad(5);
        topTenTable.row();
        topTenTable.addSeparator();

        if (topTenList != null) {
            for (Player player : topTenList) {
                topTenTable.row();
                topTenTable.add(new VisLabel(player.toString()));
            }
        }

        // action button table
        actionsTable.setPosition(0, -200);
        actionsTable.row();
        actionsTable.add(mainMenuBtn).center().height(40).width(100).pad(5);
        actionsTable.add(exitBtn).center().height(40).width(60).pad(5);
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
