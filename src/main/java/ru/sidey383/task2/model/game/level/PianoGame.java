package ru.sidey383.task2.model.game.level;

import ru.sidey383.task2.model.game.ClickType;
import ru.sidey383.task2.model.game.TileLinesGame;
import ru.sidey383.task2.model.game.level.line.TileLine;
import ru.sidey383.task2.model.game.level.line.TileLineContainer;
import ru.sidey383.task2.model.game.level.line.tile.Tile;
import ru.sidey383.task2.model.game.level.line.tile.TileStatus;

import java.util.*;
import java.util.function.Function;

public class PianoGame extends AbstractTimerGame implements TileLinesGame {


    private final String name;
    HashMap<ClickType, TileLine> lines = new HashMap<>();

    private final List<Function<Collection<TileStatus>, Void>> resultListeners = new ArrayList<>();

    /**
     * use nano time
     * **/
    private final long totalTime;

    public PianoGame(String name, long time, Map<ClickType, Tile[]> tiles) {
        this.name = name;
        this.totalTime = time;
        for (Map.Entry<ClickType, Tile[]> entry : tiles.entrySet()) {
            try {
                lines.put(entry.getKey(), new TileLineContainer(this, Arrays.asList(entry.getValue())));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Illegal state on line "+ entry.getKey(), e);
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        Collection<TileStatus> statuses = getStatistic();
        for (var listener : resultListeners) {
            listener.apply(new ArrayList<>(statuses));
        }
    }

    @Override
    public void press(ClickType type, long globalTime) {
        TileLine line = lines.get(type);
        if (line != null && isOn() && !isPaused())
            line.press(globalTime);
    }

    @Override
    public void release(ClickType type, long globalTime) {
        TileLine line = lines.get(type);
        if (line != null && isOn() && !isPaused())
            line.release(globalTime);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public TileLine getLine(ClickType clickType) {
        return lines.get(clickType);
    }

    @Override
    public ClickType[] getAvailableTypes() {
        return new ClickType[] {ClickType.LINE_1, ClickType.LINE_2, ClickType.LINE_3, ClickType.LINE_4, ClickType.LINE_5, ClickType.LINE_6};
    }

    /**
     * @return nano time
     * **/
    @Override
    public long getTimeToShow() {
        return 1_000_000_000 ;
    }

    @Override
    public Collection<TileStatus> getStatistic() {
        Collection<TileStatus> collection = new ArrayList<>();
        for (TileLine line : lines.values())
            line.getStatistic(collection);
        return collection;
    }

    /**
     * @return nano time
     * **/
    @Override
    public long getTotalTime() {
        return totalTime;
    }

    public void addResultListener(Function<Collection<TileStatus>, Void> listener) {
        resultListeners.add(listener);
    }

}
