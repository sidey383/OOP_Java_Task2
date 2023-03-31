package ru.sidey383.model.game;

import ru.sidey383.model.game.line.TileLine;
import ru.sidey383.model.game.line.TileLineContainer;
import ru.sidey383.model.game.tile.Tile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PianoGame extends AbstractTimerGame implements TileLinesGame {

    HashMap<ClickType, TileLine> lines;

    private final long totalTime;

    public PianoGame(long time, Map<ClickType, Tile[]> tiles) {
        for (Map.Entry<ClickType, Tile[]> entry : tiles.entrySet()) {
            lines.put(entry.getKey(), new TileLineContainer(this, Arrays.asList(entry.getValue())));
        }
        this.totalTime = time;
    }

    @Override
    public void press(ClickType type, long globalTime) {
        lines.get(type).press(globalTime);
    }

    @Override
    public void release(ClickType type, long globalTime) {
        lines.get(type).press(globalTime);
    }

    @Override
    public TileLine getLine(ClickType clickType) {
        return lines.get(clickType);
    }

    @Override
    public ClickType[] getAvailableTypes() {
        return new ClickType[] {ClickType.LINE_1, ClickType.LINE_2, ClickType.LINE_3, ClickType.LINE_4};
    }

    @Override
    public long getTimeToShow() {
        return 3000;
    }

    public long getTotalTime() {
        return totalTime;
    }

}
