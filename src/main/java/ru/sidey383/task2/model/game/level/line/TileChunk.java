package ru.sidey383.task2.model.game.level.line;

import ru.sidey383.task2.model.game.level.line.tile.Tile;
import ru.sidey383.task2.model.game.level.line.tile.TileStatus;

import java.util.Arrays;
import java.util.Collection;

public class TileChunk {

    Tile[] tiles;

    public TileChunk(Tile[] tiles) {
        this.tiles = tiles;
    }

    public void getTiles(Collection<Tile> tileCollection, long startTime, long endTime) {
        for (Tile t : tiles) {
            if (t.endTime() >= startTime && t.startTime()  <= endTime) {
                tileCollection.add(t);
            }
        }
    }

    public Tile getTile(long time) {
        for (Tile tile : tiles) {
            if (tile.endTime() <= time)
                continue;
            if (tile.startTime() >= time)
                break;
            return tile;
        }
        return null;
    }

    public long getScore() {
        return Arrays.stream(tiles).map(Tile::getStatus).mapToLong(TileStatus::score).sum();
    }

    public void getTileStatistic(Collection<TileStatus> statuses) {
        for (Tile t : tiles)
            statuses.add(t.getStatus());
    }

}
