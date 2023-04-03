package ru.sidey383.model.game.level.line;

import ru.sidey383.model.game.level.line.tile.Tile;
import ru.sidey383.model.game.level.line.tile.TileStatus;

import java.util.Collection;

public interface TileLine {

    Collection<Tile> getTiles(long startTime, long endTime);

    void getTiles(Collection<Tile> tileCollection, long startTime, long endTime);

    void press(long time);

    void release(long time);

    Tile getTile(long time);

    Collection<TileStatus> getStatistic();

    void getStatistic(Collection<TileStatus> statuses);

}
