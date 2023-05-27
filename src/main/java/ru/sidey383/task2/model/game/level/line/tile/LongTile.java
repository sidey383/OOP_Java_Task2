package ru.sidey383.task2.model.game.level.line.tile;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("LongTile")
public class LongTile implements Tile {

    private final long startTime;

    private final long tileTime;

    private long pressTime = -1;

    private long releaseTime = -1;

    @JsonCreator
    public LongTile(@JsonProperty("startTime") long startTime, @JsonProperty("tileTime") long tileTime) {
        this.startTime = startTime;
        this.tileTime = tileTime;
    }

    @JsonProperty("tileTime")
    public long tileTime() {
        return tileTime;
    }

    @JsonProperty("startTime")
    @Override
    public long startTime() {
        return startTime;
    }

    @Override
    public long endTime() {
        return startTime + tileTime;
    }

    @JsonIgnore
    @Override
    public LongTileStatus getStatus() {
        return new LongTileStatus(this);
    }

    @Override
    public TileType type() {
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

    private record LongTileStatus(boolean clicked, int score) implements TileStatus {

        public LongTileStatus(LongTile tile) {
            this(
                    tile.pressTime != -1,
                    tile.pressTime == -1 ?
                            0 :
                            (int) (Math.max(0, Math.min(tile.tileTime, tile.pressTime - tile.releaseTime) / 200_000_000) + 2)
            );
        }

    }
}
