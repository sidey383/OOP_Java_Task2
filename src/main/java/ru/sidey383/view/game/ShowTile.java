package ru.sidey383.view.game;

import ru.sidey383.model.intarface.tile.Tile;

public class ShowTile {

    private final Tile tile;

    private double start;

    private double end;

    private final long deathTime;

    private final long showStartTime;

    public ShowTile(long gameTime, long showStartTime, long deathTime, Tile tile) {
        this.tile = tile;
        this.showStartTime = showStartTime;
        this.deathTime = deathTime;
        start = (double) (tile.getStartTime() - gameTime) / showStartTime;
        end = (double) (tile.getEndTime() - gameTime) / showStartTime;
    }

    public Tile getTile() {
        return tile;
    }

    public boolean updatePose(long gameTime) {
        start = (double) (tile.getStartTime() - gameTime) / showStartTime;
        long endPose = tile.getEndTime() - gameTime;
        end = (double) (endPose) / showStartTime;
        return endPose > deathTime;
    }

    public double getEnd() {
        return end;
    }

    public double getStart() {
        return start;
    }

}
