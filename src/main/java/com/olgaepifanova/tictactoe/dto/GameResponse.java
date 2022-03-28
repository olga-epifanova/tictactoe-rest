package com.olgaepifanova.tictactoe.dto;

import com.olgaepifanova.tictactoe.model.Player;

public class GameResponse {
    private GameStatus status;
    private Player winner;
    private char[][] cells;

    public GameResponse(GameStatus status, Player winner, char[][] cells) {
        this.status = status;
        this.winner = winner;
        this.cells = cells;
    }

    public GameResponse(GameStatus status, char[][] cells) {
        this(status, null, cells);
    }

    public char[][] getCells() {
        return cells;
    }

    public GameStatus getStatus() {
        return status;
    }

    public Player getWinner() {
        return winner;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

}
