package com.olgaepifanova.tictactoe.exception;

public class GameOverException extends RuntimeException {

    public GameOverException() {
        super("The game is over");
    }

}
