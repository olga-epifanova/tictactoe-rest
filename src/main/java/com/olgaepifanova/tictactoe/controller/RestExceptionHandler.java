package com.olgaepifanova.tictactoe.controller;

import com.olgaepifanova.tictactoe.exception.CellIsNotFreeException;
import com.olgaepifanova.tictactoe.exception.GameNotFoundException;
import com.olgaepifanova.tictactoe.exception.GameOverException;
import com.olgaepifanova.tictactoe.exception.InvalidCoordinateValuesException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<String> gameNotFoundExceptionHandler(GameNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(CellIsNotFreeException.class)
    public ResponseEntity<String> cellIsNotFreeExceptionHandler(CellIsNotFreeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(InvalidCoordinateValuesException.class)
    public ResponseEntity<String> InvalidCoordinateValuesExceptionHandler(InvalidCoordinateValuesException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(GameOverException.class)
    public ResponseEntity<String> GameOverExceptionHandler(GameOverException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
