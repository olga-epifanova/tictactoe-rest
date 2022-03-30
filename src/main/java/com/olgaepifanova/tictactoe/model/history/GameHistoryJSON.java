package com.olgaepifanova.tictactoe.model.history;

import com.olgaepifanova.tictactoe.model.Player;
import com.olgaepifanova.tictactoe.model.Step;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.FileWriter;
import java.io.IOException;

public class GameHistoryJSON extends GameHistoryFile {

    public GameHistoryJSON(Player firstPlayer, Player secondPlayer) {
        super(firstPlayer, secondPlayer);
    }

    @Override
    public void createHistoryFile(String gameId) {

        JSONObject firstPlayerObj = new JSONObject();
        addPlayer(firstPlayer, firstPlayerObj);

        JSONObject secondPlayerObj = new JSONObject();
        addPlayer(secondPlayer, secondPlayerObj);

        JSONArray players = new JSONArray();
        players.put(firstPlayerObj);
        players.put(secondPlayerObj);

        JSONArray stepArr = new JSONArray();
        addSteps(stepArr);
        JSONObject gameObj = new JSONObject();
        gameObj.put("Step", stepArr);

        JSONObject gameResultObj = new JSONObject();
        JSONObject winnerObj = new JSONObject();
        if (winner != null) {
            addPlayer(winner, winnerObj);
            gameResultObj.put("Player", winnerObj);
        }

        JSONObject gameplayJson = new JSONObject();
        gameplayJson.put("Player", players);
        gameplayJson.put("Game", gameObj);
        gameplayJson.put("GameResult", gameResultObj);

        JSONObject resultJson = new JSONObject();
        resultJson.put("Gameplay", gameplayJson);

        try {
            FileWriter file = new FileWriter("game" + gameId + ".json");
            file.write(resultJson.toString(4));
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addSteps(JSONArray stepArr) {
        int num = 1;
        for (Step step : steps) {
            JSONObject stepJson = new JSONObject();
            String playerId = "" + step.getPlayer().getplayerNumber();
            stepJson.put("num", num++);
            stepJson.put("playerId", playerId);
            stepJson.put("coordinateX", step.getCoordinateX());
            stepJson.put("coordinateY", step.getCoordinateY());
            stepArr.put(stepJson);
        }
    }

    private void addPlayer(Player player, JSONObject playerObj) {
        playerObj.put("id", player.getplayerNumber());
        playerObj.put("name", player.getPlayerName());
        playerObj.put("symbol", "" + player.getPlayerSign());
    }

    @Override
    public void addStep(Step step) {
        steps.add(step);
    }

    @Override
    public void setWinner(Player player) {
        winner = player;
    }

}


