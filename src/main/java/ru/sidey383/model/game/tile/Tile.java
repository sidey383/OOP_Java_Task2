package ru.sidey383.model.game.tile;

public interface Tile {

    long getStartTime();

    long getEndTime();

    TileStatus getStatus();

    void press(long relativeTime);

    void release(long relativeTime);

}
