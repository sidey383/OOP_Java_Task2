package ru.sidey383.model.intarface.tile;

public interface Tile {

    long getStartTime();

    long getEndTime();

    TileStatus getStatus();

    void press();

    void release();

    void onEnd();

}
