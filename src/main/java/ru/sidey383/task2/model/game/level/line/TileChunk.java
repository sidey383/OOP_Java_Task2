package ru.sidey383.task2.model.game.level.line;

import ru.sidey383.task2.model.game.level.line.tile.Tile;
import ru.sidey383.task2.model.game.level.line.tile.TileStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TileChunk implements TileLine {

    Tile[] tiles;

    private final long relativeStartTime;

    private final long relativeEndTime;

    public TileChunk(long relativeStartTime, long relativeEndTime, Tile[] tiles) {
        this.relativeStartTime = relativeStartTime;
        this.relativeEndTime = relativeEndTime;
        this.tiles = tiles;
    }


    @Override
    public Collection<Tile> getTiles(long startTime, long endTime) {
        List<Tile> tiles = new ArrayList<>();
        getTiles(tiles, startTime, endTime);
        return tiles;
    }

    @Override
    public void getTiles(Collection<Tile> tileCollection, long startTime, long endTime) {
        for (Tile t : tiles) {
            if (t.getEndTime() >= startTime && t.getStartTime()  <= endTime) {
                tileCollection.add(t);
            }
        }
    }

    @Override
    public void press(long time) {
        Tile t = getTile(time);
        if (t != null)
            t.press(time);
    }

    @Override
    public void release(long time) {
        Tile t = getTile(time);
        if (t != null)
            t.release(time);
    }

    @Override
    public Tile getTile(long time) {
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i].getEndTime() <= time)
                continue;
            if (tiles[i].getStartTime() >= time)
                break;
            return tiles[i];
        }
        return null;
    }

    @Override
    public Collection<TileStatus> getStatistic() {
        Collection<TileStatus> collection = new ArrayList<>();
        getStatistic(collection);
        return collection;
    }

    @Override
    public void getStatistic(Collection<TileStatus> statuses) {
        for (Tile t : tiles)
            statuses.add(t.getStatus());
    }

    public long getRelativeStartTime() {
        return relativeStartTime;
    }

    public long getRelativeEndTime() {
        return relativeEndTime;
    }
}
