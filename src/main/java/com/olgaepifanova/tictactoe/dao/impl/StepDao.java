package com.olgaepifanova.tictactoe.dao.impl;

import com.olgaepifanova.tictactoe.dao.IStepDao;
import com.olgaepifanova.tictactoe.model.Step;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class StepDao implements IStepDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public StepDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long create(Step step, Long gameId, Long playerId) {

        String sql = "SELECT MAX(NUMBER) FROM STEP WHERE GAME_ID = :gameId";
        SqlParameterSource namedParameters = new MapSqlParameterSource("gameId", gameId);
        Integer queryResult = jdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
        int number = (queryResult != null ? queryResult : 0);

        SqlParameterSource inputParam = new MapSqlParameterSource()
                .addValue("x", step.getCoordinateX())
                .addValue("y", step.getCoordinateY())
                .addValue("number", number+1)
                .addValue("player_id", playerId)
                .addValue("game_id", gameId);

        Long stepId = (Long) new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("step")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(inputParam);

        return stepId;
    }
}
