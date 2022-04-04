package com.olgaepifanova.tictactoe.model.history;

import com.olgaepifanova.tictactoe.model.Player;
import com.olgaepifanova.tictactoe.model.Step;

import java.util.ArrayList;

public abstract class GameHistoryFile {

    protected final long gameId;
    protected final Player firstPlayer;
    protected final Player secondPlayer;
    protected final ArrayList<Step> steps = new ArrayList<>();
    protected Player winner;

    public GameHistoryFile(long gameId, Player firstPlayer, Player secondPlayer) {
        this.gameId = gameId;
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    public long getGameId() {
        return gameId;
    }

    public abstract void createHistoryFile();
    public abstract void addStep(Step step);
    public abstract void setWinner(Player player);

}
