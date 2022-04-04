package com.olgaepifanova.tictactoe.service;

import com.olgaepifanova.tictactoe.dao.IGameDao;
import com.olgaepifanova.tictactoe.dao.IStepDao;
import com.olgaepifanova.tictactoe.dto.CurrentGameState;
import com.olgaepifanova.tictactoe.dto.GameHistory;
import com.olgaepifanova.tictactoe.dto.GameResponse;
import com.olgaepifanova.tictactoe.dto.GameStatus;
import com.olgaepifanova.tictactoe.exception.CellIsNotFreeException;
import com.olgaepifanova.tictactoe.exception.GameNotFoundException;
import com.olgaepifanova.tictactoe.exception.InvalidCoordinateValuesException;
import com.olgaepifanova.tictactoe.model.*;
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

@Service
public class GameService {

    private final IGameDao gameDao;
    private final IStepDao stepDao;

    public GameService(IGameDao gameDao, IStepDao stepDao) {
        this.gameDao = gameDao;
        this.stepDao = stepDao;
    }

    private List<GameHistoryFile> gameHistoryList = new ArrayList<>();

    public Long createGame(String firstPlayerName,
                           String secondPlayerName) {

        Player firstPlayer = new Player(1, firstPlayerName, 'X', true);
        Player secondPlayer = new Player(2, secondPlayerName, 'O', false);

        Game game = new Game(firstPlayer, secondPlayer, new GameField());

        Long gameId = gameDao.create(game);
        gameHistoryList.add(new GameHistoryXML(gameId, firstPlayer, secondPlayer));
        gameHistoryList.add(new GameHistoryJSON(gameId, firstPlayer, secondPlayer));

        return gameId;
    }

    public GameResponse makeMove(int x, int y, Long gameId) {
        Game game = gameDao.getGame(gameId);

        if (x < 1 || x > 3 || y < 1 || y > 3) {
            throw new InvalidCoordinateValuesException();
        }

        if (game.isBusyCell(x, y)) {
            throw new CellIsNotFreeException();
        }

        Player currentPlayer = (game.getFirstPlayer().isCurrentPlayer()) ? game.getFirstPlayer() : game.getSecondPlayer();

        game.getGameField().setCell(x, y, currentPlayer.getPlayerSign());
        Step step = new Step(currentPlayer, x, y);
        gameHistoryList.forEach(gameHistory -> {
            if (gameHistory.getGameId() == gameId) {
                gameHistory.addStep(step);
            }
        });
        Long stepId = stepDao.create(step, gameId, currentPlayer.getPlayerId());

        if (game.hasWinner(currentPlayer)) {
            gameDao.updateGameStatus(gameId, GameStatus.WIN);
            FileResultWriter.writeFile("Победитель: " + currentPlayer.getPlayerName());
            gameHistoryList.forEach(gameHistory -> {
                if (gameHistory.getGameId() == gameId) {
                    gameHistory.setWinner(currentPlayer);
                    gameHistory.createHistoryFile();
                }
            });
            return new GameResponse(GameStatus.WIN, currentPlayer, game.getGameField().getCells());
        }

        if (game.isDraw()) {
            gameDao.updateGameStatus(gameId, GameStatus.DRAW);
            gameHistoryList.forEach(gameHistory -> {
                if (gameHistory.getGameId() == gameId) {
                    gameHistory.setWinner(null);
                    gameHistory.createHistoryFile();
                }
            });
            return new GameResponse(GameStatus.DRAW, game.getGameField().getCells());
        }

        gameDao.changeCurrentPlayer(game);

        return new GameResponse(GameStatus.MOVE_DONE, game.getGameField().getCells());
    }

    public CurrentGameState getCurrentGameState(Long gameId) {

        Game game = gameDao.getGame(gameId);
        Player currentPlayer = (game.getFirstPlayer().isCurrentPlayer()) ? game.getFirstPlayer() : game.getSecondPlayer();

        if (game == null) {
            throw new GameNotFoundException();
        }

        return new CurrentGameState(currentPlayer, game.getGameField().getCells());
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
