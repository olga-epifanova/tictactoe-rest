package com.olgaepifanova.tictactoe.dao;

import com.olgaepifanova.tictactoe.dto.GameStatus;
import com.olgaepifanova.tictactoe.model.Game;

public interface IGameDao {

    Long create(Game game);
    Game getGame(Long gameId);
    void changeCurrentPlayer(Game game);
    void updateGameStatus(Long gameId, GameStatus status);

}
