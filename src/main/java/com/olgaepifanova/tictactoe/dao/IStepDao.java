package com.olgaepifanova.tictactoe.dao;

import com.olgaepifanova.tictactoe.model.Step;

public interface IStepDao {

    Long create(Step step, Long gameId, Long playerId);

}
