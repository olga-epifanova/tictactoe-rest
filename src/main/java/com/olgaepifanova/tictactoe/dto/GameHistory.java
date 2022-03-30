package com.olgaepifanova.tictactoe.dto;

import com.olgaepifanova.tictactoe.model.Player;

import java.util.ArrayList;
import java.util.List;

public class GameHistory {

    private Player firstPlayer;
    private Player secondPlayer;
    private List<CurrentGameState> gameHistory = new ArrayList<>();
    private Player winner;

    public List<CurrentGameState> getGameHistory() {
        return gameHistory;
    }

    public void setGameHistory(List<CurrentGameState> gameHistory) {
        this.gameHistory = gameHistory;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(Player firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(Player secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }
}
