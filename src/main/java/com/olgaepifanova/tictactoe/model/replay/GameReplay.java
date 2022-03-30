package com.olgaepifanova.tictactoe.model.replay;

import com.olgaepifanova.tictactoe.dto.CurrentGameState;
import com.olgaepifanova.tictactoe.dto.GameHistory;
import com.olgaepifanova.tictactoe.model.GameField;
import com.olgaepifanova.tictactoe.model.Step;

import java.io.File;
import java.util.List;

public abstract class GameReplay {

    public abstract GameHistory parseFile(File file);

    protected void setSteps(List<Step> steps, List<CurrentGameState> gameHistoryList) {
        GameField gameField = new GameField();
        for (Step step : steps) {
            int y = step.getCoordinateY();
            int x = step.getCoordinateX();
            char sign = step.getPlayer().getPlayerSign();
            gameField.setCell(x - 1, y - 1, sign);
            char[][] cells = copyArr(gameField.getCells());
            CurrentGameState gameState = new CurrentGameState(step.getPlayer(), cells);
            gameHistoryList.add(gameState);
        }
    }

    protected char[][] copyArr(char[][] arr) {
        char[][] cells = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[j][i] = arr[j][i];
            }
        }
        return cells;
    }

}
