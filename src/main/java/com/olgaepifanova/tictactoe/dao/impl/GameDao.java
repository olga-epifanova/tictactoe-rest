package com.olgaepifanova.tictactoe.dao.impl;

import com.olgaepifanova.tictactoe.dao.IGameDao;
import com.olgaepifanova.tictactoe.dto.GameStatus;
import com.olgaepifanova.tictactoe.exception.GameNotFoundException;
import com.olgaepifanova.tictactoe.exception.GameOverException;
import com.olgaepifanova.tictactoe.model.*;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GameDao implements IGameDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public GameDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long create(Game game) {
        SqlParameterSource inputParam = new MapSqlParameterSource()
                .addValue("status", GameStatus.IN_PROGRESS);

        Long gameId = (Long) new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("game")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(inputParam);

        SqlParameterSource inputParamFirstPlayer = getInputParamPlayer(game.getFirstPlayer(), gameId);
        SqlParameterSource inputParamSecondPlayer = getInputParamPlayer(game.getSecondPlayer(), gameId);

        SqlParameterSource[] inputParamPlayer = {inputParamFirstPlayer, inputParamSecondPlayer};

        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("player")
                .usingGeneratedKeyColumns("id")
                .executeBatch(inputParamPlayer);

        return gameId;
    }

    private SqlParameterSource getInputParamPlayer(Player player, Long gameId) {
        return new MapSqlParameterSource()
                .addValue("name", player.getPlayerName())
                .addValue("number", player.getPlayerNumber())
                .addValue("is_current", player.isCurrentPlayer())
                .addValue("game_id", gameId);
    }

    @Override
    public Game getGame(Long gameId) {

        checkGameExistence(gameId);
        checkGameStatus(gameId);

        List<Player> players = getPlayers(gameId);
        List<Step> steps = getSteps(gameId, players);

        Player firstPlayer = null;
        Player secondPlayer = null;
        for (Player player : players) {
            if (player.getPlayerNumber() == 1) {
                firstPlayer = player;
            } else if (player.getPlayerNumber() == 2) {
                secondPlayer = player;
            }
        }

        GameField gameField = new GameField();
        for (Step step : steps) {
            gameField.setCell(step.getCoordinateX(), step.getCoordinateY(), step.getPlayer().getPlayerSign());
        }

        return new Game(firstPlayer, secondPlayer, gameField);
    }

    @Override
    public void changeCurrentPlayer(Game game) {
        String updateQuery = "UPDATE PLAYER SET is_current = NOT is_current WHERE id = :id";

        long firstPlayerId = game.getFirstPlayer().getPlayerId();
        SqlParameterSource paramFirstPlayer = new MapSqlParameterSource("id", firstPlayerId);
        jdbcTemplate.update(updateQuery, paramFirstPlayer);

        long secondPlayerId = game.getSecondPlayer().getPlayerId();
        SqlParameterSource paramSecondPlayer = new MapSqlParameterSource("id", secondPlayerId);
        jdbcTemplate.update(updateQuery, paramSecondPlayer);
    }

    @Override
    public void updateGameStatus(Long gameId, GameStatus status) {
        String updateQuery = "UPDATE GAME SET status = :status WHERE id = :gameId";
        SqlParameterSource inputParam = new MapSqlParameterSource()
                .addValue("status", status.name())
                .addValue("gameId", gameId);
        jdbcTemplate.update(updateQuery, inputParam);
    }

    private List<Player> getPlayers(Long gameId) {
        SqlParameterSource parameters = new MapSqlParameterSource("gameId", gameId);

        return jdbcTemplate.query(
                "SELECT * FROM PLAYER WHERE GAME_ID = :gameId",
                parameters,
                (rs, rowNum) -> new Player(rs.getLong("id"), rs.getInt("number"), rs.getString("name")
                        , (rs.getInt("number") == 1) ? 'X' : '0', rs.getBoolean("is_current")));
    }

    private List<Step> getSteps(Long gameId, List<Player> players) {
        SqlParameterSource parameters = new MapSqlParameterSource("gameId", gameId);

        return jdbcTemplate.query(
                "SELECT * FROM STEP WHERE GAME_ID = :gameId ORDER BY number",
                parameters,
                (rs, rowNum) -> {
                    long playerId = rs.getLong("player_id");
                    Player player = players.stream().filter(p -> p.getPlayerId() == playerId).findFirst().get();
                    return new Step(player, rs.getInt("x"), rs.getInt("y"));
                }
        );
    }

    private void checkGameExistence(Long gameId) {
        String sql = "SELECT EXISTS(SELECT ID FROM GAME WHERE ID = :gameId)";
        SqlParameterSource namedParameters = new MapSqlParameterSource("gameId", gameId);
        boolean isGameExist = jdbcTemplate.queryForObject(sql, namedParameters, Boolean.class);
        if (!isGameExist) {
            throw new GameNotFoundException();
        }
    }

    private void checkGameStatus(Long gameId) {
        String sql = "SELECT status FROM GAME WHERE ID = :gameId";
        SqlParameterSource namedParameters = new MapSqlParameterSource("gameId", gameId);
        String gameStatus = jdbcTemplate.queryForObject(sql, namedParameters, String.class);
        if (!gameStatus.equals("IN_PROGRESS")) {
            throw new GameOverException();
        }
    }

}
