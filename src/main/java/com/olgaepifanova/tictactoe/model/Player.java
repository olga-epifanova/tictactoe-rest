package com.olgaepifanova.tictactoe.model;

public class Player {

    private long playerId;
    private final int playerNumber;
    private final String playerName;
    private final char playerSign;
    private boolean isCurrentPlayer;

    public String getPlayerName() {
        return playerName;
    }

    public char getPlayerSign() {
        return playerSign;
    }

    public long getPlayerId() {
        return playerId;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerId(long id) {
        this.playerId = id;
    }

    public Player(int number, String name, char sign) {
        this.playerNumber = number;
        this.playerName = name;
        this.playerSign = sign;
    }

    public Player(int number, String name, char sign, boolean isCurrentPlayer) {
        this.playerNumber = number;
        this.playerName = name;
        this.playerSign = sign;
        this.isCurrentPlayer = isCurrentPlayer;
    }

    public Player(long playerId, int number, String name, char sign, boolean isCurrentPlayer) {
        this.playerId = playerId;
        this.playerNumber = number;
        this.playerName = name;
        this.playerSign = sign;
        this.isCurrentPlayer = isCurrentPlayer;
    }

    public boolean isCurrentPlayer() {
        return isCurrentPlayer;
    }

}
