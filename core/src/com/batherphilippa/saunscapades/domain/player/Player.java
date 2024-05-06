package com.batherphilippa.saunscapades.domain.player;

import com.opencsv.bean.CsvBindByPosition;

/**
 * Player - define el jugador del juego; usado para guardar puntación.
 */
public class Player {

    @CsvBindByPosition(position = 0)
    private String playerName;

    @CsvBindByPosition(position = 1)
    private int playerScore;


    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }


    @Override
    public String toString() {
        return playerName + " : " + playerScore;
    }
}
