package com.olgaepifanova.tictactoe;

import com.olgaepifanova.tictactoe.dao.IGameDao;
import com.olgaepifanova.tictactoe.dao.IStepDao;
import com.olgaepifanova.tictactoe.dto.CurrentGameState;
import com.olgaepifanova.tictactoe.dto.GameResponse;
import com.olgaepifanova.tictactoe.dto.GameStatus;
import com.olgaepifanova.tictactoe.model.Game;
import com.olgaepifanova.tictactoe.model.GameField;
import com.olgaepifanova.tictactoe.model.Player;
import com.olgaepifanova.tictactoe.service.GameService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class GameServiceTest {
    private final IGameDao gameDao = Mockito.mock(IGameDao.class);
    private final IStepDao stepDao = Mockito.mock(IStepDao.class);
    private final GameService gameService = new GameService(gameDao, stepDao);

    @Test
    void createGameTest() {
        Long gameId = 1L;

        given(gameDao.create(any()))
                .willReturn(gameId);

        Long result = gameService.createGame("name1", "name2");

        then(gameDao)
                .should()
                .create(any(Game.class));

        Assertions.assertEquals(gameId, result);
    }

    @Test
    void makeMoveTest() {

        Player firstPlayer = new Player(1, "Olga", 'X', true);
        Player secondPlayer = new Player(2, "Bob", 'O', false);
        Game game = new Game(firstPlayer, secondPlayer, new GameField());

        given(gameDao.getGame(any()))
                .willReturn(game);

        char[][] cells = {
                {'-', '-', '-'},
                {'-', 'X', '-'},
                {'-', '-', '-'}
        };
        GameResponse expectedGameResponse = new GameResponse(GameStatus.MOVE_DONE, cells);

        GameResponse result = gameService.makeMove(2, 2, 1L);

        Assertions.assertEquals(expectedGameResponse.getStatus(), result.getStatus());
        Assertions.assertArrayEquals(expectedGameResponse.getCells(), result.getCells());
    }

    @Test
    void getCurrentGameState() {

        Player firstPlayer = new Player(1, "Olga", 'X', true);
        Player secondPlayer = new Player(2, "Bob", 'O', false);
        Game game = new Game(firstPlayer, secondPlayer, new GameField());

        given(gameDao.getGame(any()))
                .willReturn(game);

        CurrentGameState expectedGameState = new CurrentGameState(firstPlayer, new GameField().getCells());

        CurrentGameState result = gameService.getCurrentGameState(1L);

        Assertions.assertArrayEquals(expectedGameState.getCells(), result.getCells());
        Assertions.assertEquals(expectedGameState.getCurrentPlayer().getPlayerName(), result.getCurrentPlayer().getPlayerName());
        Assertions.assertEquals(expectedGameState.getCurrentPlayer().getPlayerNumber(), result.getCurrentPlayer().getPlayerNumber());
    }

}
