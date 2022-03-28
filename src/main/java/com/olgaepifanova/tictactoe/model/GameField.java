package com.olgaepifanova.tictactoe.model;

public class GameField {

    private final char[][] cells = {
            {'-', '-', '-'},
            {'-', '-', '-'},
            {'-', '-', '-'}
    };
    private static final char defaultCellSign = '-';

    public static char getDefaultCellSign() {
        return defaultCellSign;
    }

    public void setCell(int x, int y, char sign) {
        cells[y][x] = sign;
    }

    public char getCell(int x, int y) {
        return cells[y][x];
    }

    public char[][] getCells() {
        return cells;
    }

    public boolean isFullGameField() {
        boolean isFull = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cells[i][j] == defaultCellSign) {
                    isFull = false;
                    break;
                };
            }
        }
        return isFull;
    }

}
