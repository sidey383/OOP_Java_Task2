package ru.sidey383.task2.model.game.level.line;

import ru.sidey383.task2.model.game.level.line.tile.Tile;
import ru.sidey383.task2.model.game.level.line.tile.TileStatus;
import ru.sidey383.task2.model.game.level.line.tile.TileType;

import java.util.ArrayList;
import java.util.List;

public class ClickCollectorTile implements Tile {

    private final long startTime;
    private final long endTime;

    public final List<Long> pressTime = new ArrayList<>();

    public final List<Long> realizeTime = new ArrayList<>();

    public ClickCollectorTile(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public long startTime() {
        return startTime;
    }

    @Override
    public long endTime() {
        return endTime;
    }

    @Override
    public TileStatus getStatus() {
        return null;
    }

    @Override
    public void press(long relativeTime) {
        pressTime.add(relativeTime);
    }

    @Override
    public void release(long relativeTime) {
        realizeTime.add(relativeTime);
    }

    @Override
    public TileType type() {
        return null;
    }
}
