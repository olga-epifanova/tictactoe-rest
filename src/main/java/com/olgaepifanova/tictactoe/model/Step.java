package com.olgaepifanova.tictactoe.model;

public class Step {

    private final Player player;
    private final int coordinateX;
    private final int coordinateY;

    public Step(Player player, int coordinateX, int coordinateY) {
        this.player = player;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public Player getPlayer() {
        return player;
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }

}
