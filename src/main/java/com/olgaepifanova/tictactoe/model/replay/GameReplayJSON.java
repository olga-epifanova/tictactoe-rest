package com.olgaepifanova.tictactoe.model.replay;

import com.olgaepifanova.tictactoe.dto.CurrentGameState;
import com.olgaepifanova.tictactoe.dto.GameHistory;
import com.olgaepifanova.tictactoe.model.Player;
import com.olgaepifanova.tictactoe.model.Step;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameReplayJSON extends GameReplay {

    @Override
    public GameHistory parseFile(File jsonFile) {

        GameHistory gameHistory = new GameHistory();

        try {
            String content = FileUtils.readFileToString(jsonFile, "utf-8");
            JSONObject fileObject = new JSONObject(content);

            JSONObject gameplayObj = fileObject.getJSONObject("Gameplay");
            JSONArray playersArr = gameplayObj.getJSONArray("Player");

            List<Player> players = new ArrayList<>();
            if (playersArr.length() > 1) {
                players.add(getPlayer(playersArr.getJSONObject(0)));
                players.add(getPlayer(playersArr.getJSONObject(1)));
                gameHistory.setFirstPlayer(getPlayer(playersArr.getJSONObject(0)));
                gameHistory.setSecondPlayer(getPlayer(playersArr.getJSONObject(1)));
            }

            JSONObject gameObj = gameplayObj.getJSONObject("Game");
            JSONArray stepArr = gameObj.getJSONArray("Step");

            List<Step> steps = new ArrayList<>();
            for (int i = 0; i < stepArr.length(); i++) {
                steps.add(getStep(stepArr.getJSONObject(i), players));
            }

            List<CurrentGameState> gameHistoryList = new ArrayList<>();
            setSteps(steps, gameHistoryList);
            gameHistory.setGameHistory(gameHistoryList);

            JSONObject gameResultObj = gameplayObj.getJSONObject("GameResult");
            if (gameResultObj.isEmpty()) {
                gameHistory.setWinner(null);
            } else {
                JSONObject winnerObj = gameResultObj.getJSONObject("Player");
                Player winnerPlayer = getPlayer(winnerObj);
                gameHistory.setWinner(winnerPlayer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return gameHistory;

    }

    private Player getPlayer(JSONObject obj) {
        int id = obj.getInt("id");
        String name = obj.getString("name");
        String symbol = obj.getString("symbol");
        char symbolChar = symbol.charAt(0);
        return new Player(id, name, symbolChar);
    }

    private Step getStep(JSONObject obj, List<Player> players) {
        int playerId = obj.getInt("playerId");
        Player playerStep = players.get(playerId - 1);
        int x = obj.getInt("coordinateX");
        int y = obj.getInt("coordinateY");
        return new Step(playerStep, x, y);
    }

}
