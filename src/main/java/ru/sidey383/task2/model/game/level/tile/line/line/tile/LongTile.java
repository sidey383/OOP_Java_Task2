package ru.sidey383.task2.model.game.level.tile.line.line.tile;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LongTile implements Tile {

    @JsonProperty
    private final long startTime;

    @JsonProperty
    private final long tileTime;

    @JsonIgnore
    private long pressTime = -1;

    @JsonIgnore
    private long releaseTime = -1;

    @JsonCreator
    public LongTile(@JsonProperty("startTime") long startTime, @JsonProperty("tileTime") long tileTime) {
        this.startTime = startTime;
        this.tileTime = tileTime;
    }

    @JsonIgnore
    @Override
    public long getStartTime() {
        return startTime;
    }

    @JsonIgnore
    @Override
    public long getEndTime() {
        return startTime + tileTime;
    }

    @JsonIgnore
    @Override
    public LongTileStatus getStatus() {
        return new LongTileStatus(this);
    }

    @JsonIgnore
    @Override
    public TileType getType() {
        return TileType.LONG;
    }

    @Override
    public void press(long relativeTime) {
        if (relativeTime < startTime || relativeTime > startTime + tileTime)
            return;
        if (relativeTime > pressTime)
            pressTime = relativeTime;
    }

    @Override
    public void release(long relativeTime) {
        if (relativeTime < startTime || relativeTime > startTime + tileTime)
            return;
        if (pressTime != -1 && (relativeTime == -1 || relativeTime < releaseTime)) {
            releaseTime = relativeTime;
        }
    }

    private record LongTileStatus(boolean isClicked, int score) implements TileStatus {

        public LongTileStatus(LongTile tile) {
            this(
                    tile.pressTime != -1,
                    tile.pressTime == -1 ?
                            0 :
                            (int) (Math.max(0, Math.min(tile.tileTime, tile.pressTime - tile.releaseTime) / 200_000_000) + 2)
            );
        }

        @Override
        public int getScore() {
            return score;
        }

    }
}
