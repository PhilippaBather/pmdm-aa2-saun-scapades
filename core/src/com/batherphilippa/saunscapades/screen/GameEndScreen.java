package com.batherphilippa.saunscapades.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.batherphilippa.saunscapades.ShaunScapades;
import com.batherphilippa.saunscapades.domain.player.Player;
import com.batherphilippa.saunscapades.manager.OptionManager;
import com.batherphilippa.saunscapades.manager.ResourceManager;
import com.batherphilippa.saunscapades.screen.util.UIUtils;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.batherphilippa.saunscapades.manager.constants.ResourcesConstants.MUSIC_GAME_FINISHED;
import static com.batherphilippa.saunscapades.screen.constants.AppConstants.CSV_PATH;
import static com.batherphilippa.saunscapades.screen.constants.UIConstants.*;

/**
 * GameEndScreen - define la pantalla que termina el juego; implementa Screen.
 */
public class GameEndScreen implements Screen, Input.TextInputListener {
    private final ShaunScapades game;
    private final ResourceManager resManager;
    private final int score;
    private Stage stage;
    private VisLabel title;
    private VisLabel scoreInfo;
    private VisLabel scoreMessage;
    private VisTextButton exitBtn;
    private VisTextButton menuBtn;
    private VisTextButton playBtn;
    private VisTextButton saveScoreBtn;
    private VisTextButton topTenBtn;

    private VisTextField userTxtFld;

    private boolean isCSVFile;
    private boolean isInputVisible;
    private List<Player> playerTopTenScoresList;

    public GameEndScreen(ShaunScapades game) {
        this.game = game;
        this.resManager = this.game.getResManager();
        this.score = ShaunScapades.score;
        this.isCSVFile = false; // establece el booleano para si el archivo CSV existe a false por defecto
        this.isInputVisible = true;  // input field visible por defecto
        this.playerTopTenScoresList = new ArrayList<>();

        readCSVFile();

        if (isCSVFile) {
            checkNewStoreAgainstTopTen();
        }

    }

    private void readCSVFile() {
        String filename = CSV_PATH;
        try {
            playerTopTenScoresList = new CsvToBeanBuilder(new FileReader(filename))
                    .withType(Player.class)
                    .build()
                    .parse();
            isCSVFile = true;
        } catch (FileNotFoundException e) {
            isCSVFile = false;
            isInputVisible = true;
        }
    }

    private void checkNewStoreAgainstTopTen() {
        playerTopTenScoresList.sort(Comparator.comparingInt(Player::getPlayerScore).reversed());
        isInputVisible = (playerTopTenScoresList.size() < 10 | (playerTopTenScoresList.size() == 10 && playerTopTenScoresList.get(9).getPlayerScore() < score));
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
        setScoreClickListener();
        createTableStructure(infoTable, actionTable);

        resManager.playMusic(MUSIC_GAME_FINISHED, false);

        Gdx.input.setInputProcessor(stage);
    }

    private void createComponents() {
        this.title = new VisLabel(LABEL_GAME_FINISHED);
        this.scoreInfo = new VisLabel(String.format(TEXT_FIELD_SCORE, score));
        this.userTxtFld = new VisTextField(TEXT_FIELD_INPUT_NAME);
        this.exitBtn = new VisTextButton(BTN_EXIT);
        this.menuBtn = new VisTextButton(BTN_MAIN_MENU);
        this.playBtn = new VisTextButton(BTN_PLAY);
        this.topTenBtn = new VisTextButton(BTN_TOP_TEN);
        this.saveScoreBtn = new VisTextButton(BTN_SAVE_SCORE);
        if (playerTopTenScoresList.size() == 10) {
            this.scoreMessage = new VisLabel(String.format(LABEL_BETTER_LUCK_NEXT_GAME, playerTopTenScoresList.get(9).getPlayerScore()));
        }
    }

    private void setClickListeners() {
        OptionManager.handleExitClicked(exitBtn, this);
        OptionManager.handleMainMenuClicked(menuBtn, game, this);
        OptionManager.handlePlayClicked(playBtn, game, this);
        OptionManager.handleTopTenClicked(topTenBtn, game, this);

    }

    private void setScoreClickListener() {
        saveScoreBtn.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                  // if list less than 10 add to list
                Player newPlayer = new Player();
                newPlayer.setPlayerName(userTxtFld.getText().trim());
                newPlayer.setPlayerScore(score);
                if (playerTopTenScoresList.size() == 10) {
                    playerTopTenScoresList.remove(9);
                }
                playerTopTenScoresList.add(newPlayer);
                playerTopTenScoresList.sort(Comparator.comparingInt(Player::getPlayerScore).reversed());
                playerTopTenScoresList.forEach(b -> System.out.println(b.toString()));
                // write to CSV
                writeCSVFile();
            }
        });
    }

    private void writeCSVFile() {
        String path = CSV_PATH;
        try (CSVWriter writer = new CSVWriter(new FileWriter(path))) {
            for(Player player : playerTopTenScoresList) {
                String[] record = {player.getPlayerName(), String.valueOf(player.getPlayerScore())};
                writer.writeNext(record);
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
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
        if (isInputVisible) {
            infoTable.row();
            infoTable.add(userTxtFld).center().height(40).pad(5);
            infoTable.row();
            infoTable.add(saveScoreBtn).center().width(125).height(30).pad(5);
            infoTable.row();
        } else {
            infoTable.row();
            infoTable.add(scoreMessage).center().height(40).pad(5);
            infoTable.row();
        }
        infoTable.addSeparator();

        // actions table
        actionsTable.setPosition(0, -175);
        actionsTable.row();
        actionsTable.add(playBtn).center().width(125).height(30).pad(5);
        actionsTable.add(topTenBtn).center().width(125).height(30).pad(5);
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

    @Override
    public void input(String text) {

    }

    @Override
    public void canceled() {

    }
}
