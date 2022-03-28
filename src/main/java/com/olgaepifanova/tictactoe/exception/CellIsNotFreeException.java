package com.olgaepifanova.tictactoe.exception;

public class CellIsNotFreeException extends RuntimeException  {

    public CellIsNotFreeException() {
        super("Cell is busy");
    }

}
