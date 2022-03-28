package com.olgaepifanova.tictactoe.dto;

import com.olgaepifanova.tictactoe.model.Player;

public class CurrentGameState {

    private Player currentPlayer;
    private char[][] cells;

    public CurrentGameState(Player currentPlayer, char[][] cells) {
        this.currentPlayer = currentPlayer;
        this.cells = cells;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public char[][] getCells() {
        return cells;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setCells(char[][] cells) {
        this.cells = cells;
    }
}
