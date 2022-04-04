package com.olgaepifanova.tictactoe.controller;

import com.olgaepifanova.tictactoe.dto.CurrentGameState;
import com.olgaepifanova.tictactoe.dto.GameHistory;
import com.olgaepifanova.tictactoe.dto.GameResponse;
import com.olgaepifanova.tictactoe.service.GameService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/gameplay")
public class GameController {

    private final GameService service;

    public GameController(GameService service) {
        this.service = service;
    }

    // http://localhost:8080/gameplay/create-game?firstPlayerName=Ольга&secondPlayerName=Игрок2
    @PostMapping(value = "/create-game")
    public ResponseEntity<String> createGame(@RequestParam String firstPlayerName,
                                             @RequestParam String secondPlayerName) {

        Long gameID = service.createGame(firstPlayerName, secondPlayerName);
        return ResponseEntity.ok(gameID.toString());
    }

    // http://localhost:8080/gameplay/make-move/1?x=1&y=1
    @PutMapping(value = "/make-move/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameResponse> makeMove(@RequestParam int x,
                                                 @RequestParam int y,
                                                 @PathVariable Long gameId) {

        GameResponse gameResponse = service.makeMove(x, y, gameId);
        return ResponseEntity.ok(gameResponse);
    }

    // http://localhost:8080/gameplay/current-game-state/1
    @GetMapping(value = "/current-game-state/{gameId}")
    public ResponseEntity<CurrentGameState> getCurrentGameState(@PathVariable Long gameId) {

        CurrentGameState currentGameState = service.getCurrentGameState(gameId);
        return ResponseEntity.ok(currentGameState);
    }

    // http://localhost:8080/gameplay/replay-game?fileName=game1.json
    @GetMapping(value = "/replay-game")
    public ResponseEntity<GameHistory> getGameHistory(@RequestParam String fileName) {
        GameHistory gameHistory = service.getGameHistory(fileName);
        return ResponseEntity.ok(gameHistory);
    }

}
