package com.olgaepifanova.tictactoe.model.history;

import com.olgaepifanova.tictactoe.model.Player;
import com.olgaepifanova.tictactoe.model.Step;

import java.util.ArrayList;

public abstract class GameHistoryFile {

    protected final Player firstPlayer;
    protected final Player secondPlayer;
    protected final ArrayList<Step> steps = new ArrayList<>();
    protected Player winner;

    public GameHistoryFile(Player firstPlayer, Player secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    public abstract void createHistoryFile(String gameId);
    public abstract void addStep(Step step);
    public abstract void setWinner(Player player);

}
