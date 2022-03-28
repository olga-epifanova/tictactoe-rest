package com.olgaepifanova.tictactoe.model;

import java.util.Objects;

public class Player {

    private final int playerNumber;
    private final String playerName;
    private final char playerSign;

    public String getPlayerName() {
        return playerName;
    }

    public char getPlayerSign() {
        return playerSign;
    }

    public int getplayerNumber() {
        return playerNumber;
    }

    public Player(int number, String name, char sign) {
        this.playerNumber = number;
        this.playerName = name;
        this.playerSign = sign;
    }

}
