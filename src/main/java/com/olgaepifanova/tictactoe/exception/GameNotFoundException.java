package com.olgaepifanova.tictactoe.exception;

public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException() {
        super("Game not found");
    }
}
