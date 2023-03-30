package ru.sidey383.model.intarface;

import ru.sidey383.model.intarface.tile.Tile;

import java.util.Collection;

public interface TileLine {

    Collection<Tile> popTiles(long viewEndTime);

    Tile[] getTiles();

    void press();

    void release();

    void onEnd();

}
