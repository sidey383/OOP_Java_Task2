package ru.sidey383.task2.model.game.level.line.tile;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DefaultTile implements Tile {

    @JsonProperty
    private final long startTime;

    @JsonIgnore
    private DefaultTileStatus tileStatus = DefaultTileStatus.NOT_CLICKED;

    @JsonCreator
    public DefaultTile(@JsonProperty("startTime") long startTime) {
        this.startTime = startTime;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    @JsonIgnore
    @Override
    public long getEndTime() {
        return startTime + 300_000_000;
    }

    @JsonIgnore
    @Override
    public TileStatus getStatus() {
        return tileStatus;
    }

    @JsonIgnore
    @Override
    public TileType getType() {
        return TileType.DEFAULT;
    }

    @Override
    public void press(long relativeTime) {
        if (relativeTime >= startTime && relativeTime <= startTime + 300_000_000)
            tileStatus = DefaultTileStatus.CLICKED;
    }

    @Override
    public void release(long relativeTime) {}

    private enum DefaultTileStatus implements TileStatus {
        NOT_CLICKED(false, 0), CLICKED(true, 3);

        private final boolean isClicked;

        private final int score;

        DefaultTileStatus(boolean isClicked, int score) {
            this.isClicked = isClicked;
            this.score = score;
        }

        @Override
        public boolean isClicked() {
            return isClicked;
        }

        @Override
        public int getScore() {
            return score;
        }
    }

}
