package ru.sidey383.task2.model.game.level.tile.line.line;

import ru.sidey383.task2.model.game.level.tile.line.PianoGame;
import ru.sidey383.task2.model.game.level.tile.line.line.tile.Tile;
import ru.sidey383.task2.model.game.level.tile.line.line.tile.TileStatus;

import java.util.*;

public class TileLineContainer implements TileLine {

    private final static long CHUNK_TIME = 5_000_000_000L;

    private final TileChunk[] tileChunks;

    private long missClickCount = 0;

    public TileLineContainer(PianoGame pianoGame, Collection<Tile> tileCollection) {
        tileChunks = new TileChunk[(int) (pianoGame.totalTime() / CHUNK_TIME + 1)];
        List<Tile> tiles = tileCollection.stream().sorted(Comparator.comparingLong(Tile::startTime)).toList();
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
    public void getTiles(Collection<Tile> tileCollection, long  startTime, long  endTime) {
        int startN = Math.max(0, (int) (startTime / CHUNK_TIME));
        int endN = Math.min(tileChunks.length, (int) (startTime / CHUNK_TIME) + 1) ;
        for (int i = startN; i < endN; i++) {
            tileChunks[i].getTiles(tileCollection, startTime, endTime);
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
        return Arrays.stream(tileChunks).mapToLong(TileChunk::getScore).sum() - missClickCount * 3;
    }

    @Override
    public void getTileStatistic(Collection<TileStatus> statuses) {
        for (TileChunk chunk : tileChunks) {
            chunk.getTileStatistic(statuses);
        }
    }

    @Override
    public long getMissCount() {
        return missClickCount;
    }
}
