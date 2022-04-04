package com.olgaepifanova.tictactoe.model.history;

import com.olgaepifanova.tictactoe.model.Player;
import com.olgaepifanova.tictactoe.model.Step;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class GameHistoryXML extends GameHistoryFile {

    public GameHistoryXML(long gameId, Player firstPlayer, Player secondPlayer) {
        super(gameId, firstPlayer, secondPlayer);
    }

    @Override
    public void createHistoryFile() {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document docGameHistory = builder.newDocument();

            Element root = docGameHistory.createElement("Gameplay");
            docGameHistory.appendChild(root);

            addPlayer(firstPlayer, docGameHistory, root);
            addPlayer(secondPlayer, docGameHistory, root);

            Element game = docGameHistory.createElement("Game");
            root.appendChild(game);
            addSteps(docGameHistory, game);

            Element gameResult = docGameHistory.createElement("GameResult");
            root.appendChild(gameResult);
            if (winner == null) {
                gameResult.appendChild(docGameHistory.createTextNode("Draw!"));
            } else {
                addPlayer(winner, docGameHistory, gameResult);
            }

            File file = new File("game" + gameId + ".xml");

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(docGameHistory), new StreamResult(file));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addSteps(Document doc, Element game) {
        int num = 1;
        for (Step step : steps) {
            Element stepElement = doc.createElement("Step");
            String playerId = "" + step.getPlayer().getPlayerNumber();
            stepElement.setAttribute("num", "" + num++);
            stepElement.setAttribute("playerId", "" + playerId);
            String coordinates = step.getCoordinateX() + " " + step.getCoordinateY();
            game.appendChild(stepElement);
            stepElement.appendChild(doc.createTextNode(coordinates));
        }
    }

    private void addPlayer(Player player, Document doc, Element elem) {
        Element playerElement = doc.createElement("Player");
        playerElement.setAttribute("id", "" + player.getPlayerNumber());
        playerElement.setAttribute("name", player.getPlayerName());
        playerElement.setAttribute("symbol", "" + player.getPlayerSign());
        elem.appendChild(playerElement);
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
