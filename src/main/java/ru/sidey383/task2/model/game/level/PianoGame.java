package ru.sidey383.task2.model.game.level;

import ru.sidey383.task2.model.game.ClickType;
import ru.sidey383.task2.model.game.level.line.TileLine;
import ru.sidey383.task2.model.game.level.line.TileLineContainer;
import ru.sidey383.task2.model.game.level.line.tile.Tile;
import ru.sidey383.task2.model.game.level.line.tile.TileStatus;

import java.util.*;

public class PianoGame extends AbstractTimerGame implements TileLinesGame {


    private final String name;
    HashMap<ClickType, TileLine> lines = new HashMap<>();

    private final List<Runnable> resultListeners = new ArrayList<>();

    /**
     * use nano time
     * **/
    private final long totalTime;

    public PianoGame(String name, long time, Map<ClickType, Tile[]> tiles) {
        this.name = name;
        this.totalTime = time;
        for (Map.Entry<ClickType, Tile[]> entry : tiles.entrySet()) {
            try {
                lines.put(entry.getKey(), new TileLineContainer(totalTime, Arrays.asList(entry.getValue())));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Illegal state on line "+ entry.getKey(), e);
            }
        }
    }

    @Override
    public boolean stop() {
        if(super.stop()) {
            for (var listener : resultListeners)
                listener.run();
            return true;
        }
        return false;
    }

    @Override
    public boolean isOutOfTime(long systemTime) {
        return toLocalTime(systemTime) >= totalTime();
    }

    @Override
    public void press(ClickType type, long globalTime) {
        if (!isGoing())
            return;
        TileLine line = lines.get(type);
        if (line != null)
            line.press(globalTime);
    }

    @Override
    public void release(ClickType type, long globalTime) {
        if (!isGoing())
            return;
        TileLine line = lines.get(type);
        if (line != null)
            line.release(globalTime);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public TileLine getLine(ClickType clickType) {
        return lines.get(clickType);
    }

    @Override
    public ClickType[] availableClickTypes() {
        return new ClickType[] {ClickType.LINE_1, ClickType.LINE_2, ClickType.LINE_3, ClickType.LINE_4, ClickType.LINE_5, ClickType.LINE_6};
    }

    /**
     * @return nano time
     * **/
    @Override
    public long timeToShow() {
        return 1_000_000_000 ;
    }

    @Override
    public Collection<TileStatus> getTileStatistic() {
        Collection<TileStatus> statuses = new ArrayList<>();
        getTileStatistic(statuses);
        return statuses;
    }

    @Override
    public void getTileStatistic(Collection<TileStatus> statuses) {
        for (TileLine line : lines.values())
            line.getTileStatistic(statuses);
    }

    @Override
    public long getMissCount() {
        return lines.values().stream().mapToLong(TileScoreProvider::getMissCount).sum();
    }

    @Override
    public long getScore() {
        return lines.values().stream().mapToLong(TileLine::getScore).sum();
    }

    /**
     * @return nano time
     * **/
    @Override
    public long totalTime() {
        return totalTime;
    }

    public void onEnd(Runnable listener) {
        if (gameStatus == GameStatus.IS_OVER)
            return;
        resultListeners.add(listener);
    }

}
