package com.olgaepifanova.tictactoe.model;

public class Game {

    private final Player firstPlayer;
    private final Player secondPlayer;
    private final GameField gameField;
    private Player currentPlayer;

    public Game(Player firstPlayer, Player secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.gameField = new GameField();
        this.currentPlayer = firstPlayer;
    }

    public GameField getGameField() {
        return gameField;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCell(int x, int y) {
        gameField.setCell(x-1, y-1, currentPlayer.getPlayerSign());
    }

    public boolean hasWinner() {
        char playerSign = currentPlayer.getPlayerSign();
        for (int i = 0; i < 3; i++) {
            if ((gameField.getCell(i, 0) == playerSign && gameField.getCell(i, 1) == playerSign && gameField.getCell(i, 2) == playerSign) ||
                    (gameField.getCell(0, i) == playerSign && gameField.getCell(1, i) == playerSign && gameField.getCell(2, i) == playerSign))
                return true;
        }

        return (gameField.getCell(0, 0) == playerSign && gameField.getCell(1, 1) == playerSign && gameField.getCell(2, 2) == playerSign) ||
                (gameField.getCell(2, 0) == playerSign && gameField.getCell(1, 1) == playerSign && gameField.getCell(0, 2) == playerSign);
    }

    public boolean isDraw() {
        return gameField.isFullGameField();
    }

    public boolean isBusyCell(int x, int y) {
        char defaultCellSign = GameField.getDefaultCellSign();
        return gameField.getCell(x - 1, y - 1) != defaultCellSign;
    }

    public void changeCurrentPlayer() {
        int currentNumber = currentPlayer.getplayerNumber();
        if (currentNumber == firstPlayer.getplayerNumber()) {
            currentPlayer = secondPlayer;
        } else {
            currentPlayer = firstPlayer;
        }
    }

}
