package ru.sidey383.task2.model.game.level.line;

import ru.sidey383.task2.model.game.level.line.tile.Tile;
import ru.sidey383.task2.model.game.level.line.tile.TileStatus;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

public class TileChunk {

    Tile[] tiles;

    public TileChunk(Tile[] tiles) {
        this.tiles = tiles;
    }

    public void getTiles(Collection<Tile> tileCollection, Predicate<Tile> predicate) {
        for (Tile t : tiles) {
            if (predicate.test(t)) {
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

    public long getScore(Predicate<Tile> predicate) {
        return Arrays.stream(tiles).filter(predicate).map(Tile::getStatus).mapToLong(TileStatus::score).sum();
    }

    public void getTileStatistic(Collection<TileStatus> statuses, Predicate<Tile> predicate) {
        for (Tile t : tiles)
            if (predicate.test(t))
                statuses.add(t.getStatus());
    }

    public void getTileStatistic(Collection<TileStatus> statuses) {
        for (Tile t : tiles)
            statuses.add(t.getStatus());
    }

}
