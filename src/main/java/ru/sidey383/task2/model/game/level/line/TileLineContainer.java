package ru.sidey383.task2.model.game.level.line;

import ru.sidey383.task2.model.game.level.line.tile.Tile;
import ru.sidey383.task2.model.game.level.line.tile.TileStatus;

import java.util.*;

public class TileLineContainer implements TileLine {

    private final static long CHUNK_TIME = 5_000_000_000L;

    private final TileChunk[] tileChunks;

    private long missClickCount = 0;

    public TileLineContainer(long totalTime, Collection<Tile> tileCollection) {
        tileChunks = new TileChunk[(int) (totalTime / CHUNK_TIME + 1)];
        List<Tile> tiles = tileCollection.stream().sorted(Comparator.comparingLong(Tile::startTime)).filter(t -> t.startTime() < totalTime && t.startTime() > 0).toList();
        for (int i = 1; i < tiles.size(); i++) {
            if (tiles.get(i - 1).endTime() > tiles.get(i).startTime())
                throw new IllegalArgumentException("Tile overlap each other, end " + tiles.get(i - 1).endTime() + " start " + tiles.get(i).startTime());
        }
        for (int i = 0; i < tileChunks.length; i++) {
            long startTime = i * CHUNK_TIME;
            long endTime = (i + 1) * CHUNK_TIME;
            tileChunks[i] = new TileChunk(tiles.stream().filter(t -> t.startTime() <= endTime && t.endTime() >= startTime).toArray(Tile[]::new));
        }
    }

    private TileChunk getTileChunk(long time) {
        if (time < 0)
            return null;
        int n = (int) (time / CHUNK_TIME);
        if (n < tileChunks.length) {
            return tileChunks[n];
        }
        return null;
    }

    @Override
    public Collection<Tile> getTiles(long startTime, long endTime) {
        Collection<Tile> tiles = new ArrayList<>();
        getTiles(tiles, startTime, endTime);
        return tiles;
    }

    @Override
    public void getTiles(Collection<Tile> tileCollection, long startTime, long endTime) {
        int startN = Math.max(0, (int) (startTime / CHUNK_TIME));
        int endN = Math.min(tileChunks.length, (int) (endTime / CHUNK_TIME) + 1);
        if (startN >= endN)
            return;
        // Collect all tiles in this chunk
        tileChunks[startN].getTiles(tileCollection, (t) -> t.endTime() >= startTime && t.startTime() <= endTime);
        for (int i = startN + 1; i < endN; i++) {
            int finalI = i;
            // Collects all tiles that are not in the previous chunks
            tileChunks[i].getTiles(tileCollection,
                    (t) ->
                            t.startTime() > finalI * CHUNK_TIME &&  // Not in previous blocks
                            (t.endTime() >= startTime && t.startTime() <= endTime)
            );
        }
    }

    @Override
    public void press(long time) {
        Tile t = getTile(time);
        if (t != null) {
            t.press(time);
        } else {
            missClickCount++;
        }
    }

    @Override
    public void release(long time) {
        Tile t = getTile(time);
        if (t != null)
            t.release(time);
    }

    @Override
    public Tile getTile(long time) {
        TileChunk chunk = getTileChunk(time);
        return chunk == null ? null : chunk.getTile(time);
    }

    @Override
    public Collection<TileStatus> getTileStatistic() {
        Collection<TileStatus> statuses = new ArrayList<>();
        getTileStatistic(statuses);
        return statuses;
    }

    @Override
    public long getScore() {
        long score = 0;
        tileChunks[0].getScore(t -> true);
        for (int i = 0; i < tileChunks.length; i++) {
            long barrierTime = i * CHUNK_TIME;
            score += tileChunks[i].getScore(p -> p.startTime() > barrierTime);
        }
        return score - missClickCount;
    }

    @Override
    public void getTileStatistic(Collection<TileStatus> statuses) {
        tileChunks[0].getTileStatistic(statuses, (t) -> true);
        for (int i = 1; i < tileChunks.length; i++) {
            long barrierTime = i * CHUNK_TIME;
            tileChunks[i].getTileStatistic(statuses, (t) -> t.startTime() > barrierTime);
        }
    }

    @Override
    public long getMissCount() {
        return missClickCount;
    }
}
