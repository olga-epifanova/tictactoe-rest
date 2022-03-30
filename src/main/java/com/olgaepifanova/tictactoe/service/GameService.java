package com.olgaepifanova.tictactoe.service;

import com.olgaepifanova.tictactoe.dto.CurrentGameState;
import com.olgaepifanova.tictactoe.dto.GameHistory;
import com.olgaepifanova.tictactoe.dto.GameResponse;
import com.olgaepifanova.tictactoe.dto.GameStatus;
import com.olgaepifanova.tictactoe.exception.CellIsNotFreeException;
import com.olgaepifanova.tictactoe.exception.GameNotFoundException;
import com.olgaepifanova.tictactoe.exception.InvalidCoordinateValuesException;
import com.olgaepifanova.tictactoe.model.FileResultWriter;
import com.olgaepifanova.tictactoe.model.Game;
import com.olgaepifanova.tictactoe.model.Player;
import com.olgaepifanova.tictactoe.model.Step;
import com.olgaepifanova.tictactoe.model.history.GameHistoryFile;
import com.olgaepifanova.tictactoe.model.history.GameHistoryJSON;
import com.olgaepifanova.tictactoe.model.history.GameHistoryXML;
import com.olgaepifanova.tictactoe.model.replay.GameReplay;
import com.olgaepifanova.tictactoe.model.replay.GameReplayJSON;
import com.olgaepifanova.tictactoe.model.replay.GameReplayXML;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {

    private Map<UUID, Game> gameMap = new ConcurrentHashMap<>();
    private List<GameHistoryFile> gameHistoryList = new ArrayList<>();

    public UUID createGame(String firstPlayerName,
                           String secondPlayerName) {

        Player firstPlayer = new Player(1, firstPlayerName, 'X');
        Player secondPlayer = new Player(2, secondPlayerName, 'O');
        gameHistoryList.add(new GameHistoryXML(firstPlayer, secondPlayer));
        gameHistoryList.add(new GameHistoryJSON(firstPlayer, secondPlayer));

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

        if (x < 1 || x > 3 || y < 1 || y > 3) {
            throw new InvalidCoordinateValuesException();
        }

        if (game.isBusyCell(x, y)) {
            throw new CellIsNotFreeException();
        }

        game.setCell(x, y);
        Step step = new Step(game.getCurrentPlayer(), x, y);
        gameHistoryList.forEach((GameHistoryFile gameHistory) -> gameHistory.addStep(step));

        if (game.hasWinner()) {
            Game currentGame = gameMap.remove(gameId);
            Player currentPlayer = currentGame.getCurrentPlayer();
            FileResultWriter.writeFile("Победитель: " + currentPlayer.getPlayerName());
            gameHistoryList.forEach((GameHistoryFile gameHistory) -> gameHistory.setWinner(currentPlayer));
            gameHistoryList.forEach((GameHistoryFile gameHistory) -> gameHistory.createHistoryFile(gameId.toString()));
            return new GameResponse(GameStatus.WIN, currentPlayer, currentGame.getGameField().getCells());
        }

        if (game.isDraw()) {
            Game currentGame = gameMap.remove(gameId);
            gameHistoryList.forEach((GameHistoryFile gameHistory) -> gameHistory.setWinner(null));
            gameHistoryList.forEach((GameHistoryFile gameHistory) -> gameHistory.createHistoryFile(gameId.toString()));
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

    public GameHistory getGameHistory(String fileName) {
        GameHistory gameHistory = new GameHistory();
        GameReplay gameReplay;
        File file = new File(fileName);
        if (file.exists()) {
            if (file.getName().endsWith(".xml")) {
                gameReplay = new GameReplayXML();
                gameHistory = gameReplay.parseFile(file);
            } else if (file.getName().endsWith(".json")) {
                gameReplay = new GameReplayJSON();
                gameHistory = gameReplay.parseFile(file);
            }
        }
        return gameHistory;
    }
}
