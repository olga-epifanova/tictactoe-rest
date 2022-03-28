package com.olgaepifanova.tictactoe.service;

import com.olgaepifanova.tictactoe.dto.CurrentGameState;
import com.olgaepifanova.tictactoe.dto.GameResponse;
import com.olgaepifanova.tictactoe.dto.GameStatus;
import com.olgaepifanova.tictactoe.exception.CellIsNotFreeException;
import com.olgaepifanova.tictactoe.exception.GameNotFoundException;
import com.olgaepifanova.tictactoe.exception.InvalidCoordinateValuesException;
import com.olgaepifanova.tictactoe.model.Game;
import com.olgaepifanova.tictactoe.model.Player;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {

    private Map<UUID, Game> gameMap = new ConcurrentHashMap<>();

    public UUID createGame(String firstPlayerName,
                           String secondPlayerName) {

        Player firstPlayer = new Player(1, firstPlayerName, 'X');
        Player secondPlayer = new Player(2, secondPlayerName, 'O');

        Game game = new Game(firstPlayer, secondPlayer);
        UUID uuid = UUID.randomUUID();

        gameMap.put(uuid, game);

        return uuid;
    }

    public GameResponse makeMove(int x, int y, UUID gameId) {
        Game game = gameMap.get(gameId);

        if (game == null) {
            throw new GameNotFoundException();
        }

        if (x<1 || x>3 || y<1 || y>3) {
            throw new InvalidCoordinateValuesException();
        }

        if (game.isBusyCell(x, y)) {
            throw new CellIsNotFreeException();
        }

        game.setCell(x, y);

        if (game.hasWinner()) {
            Game currentGame = gameMap.remove(gameId);
            return new GameResponse(GameStatus.WIN, game.getCurrentPlayer(), currentGame.getGameField().getCells());
        }

        if (game.isDraw()) {
            Game currentGame = gameMap.remove(gameId);
            return new GameResponse(GameStatus.DRAW, currentGame.getGameField().getCells());
        }

        game.changeCurrentPlayer();

        return new GameResponse(GameStatus.MOVE_DONE, game.getGameField().getCells());
    }

    public CurrentGameState getCurrentGameState(UUID gameId) {

        Game game = gameMap.get(gameId);

        if (game == null) {
            throw new GameNotFoundException();
        }

        return new CurrentGameState(game.getCurrentPlayer(), game.getGameField().getCells());

    }

}
