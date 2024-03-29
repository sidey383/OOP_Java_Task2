package ru.sidey383.task2.model.game.level.line;

import ru.sidey383.task2.model.game.level.TileScoreProvider;
import ru.sidey383.task2.model.game.level.line.tile.Tile;

import java.util.Collection;

public interface TileLine extends TileScoreProvider {

    Collection<Tile> getTiles(long startTime, long endTime);

    void getTiles(Collection<Tile> tileCollection, long startTime, long endTime);

    void press(long time);

    void release(long time);

    Tile getTile(long time);

}
