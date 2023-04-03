package ru.sidey383.model.game.level;

import ru.sidey383.model.game.ClickType;
import ru.sidey383.model.game.TileLinesGame;
import ru.sidey383.model.game.level.line.TileLine;
import ru.sidey383.model.game.level.line.TileLineContainer;
import ru.sidey383.model.game.level.line.tile.Tile;
import ru.sidey383.model.game.level.line.tile.TileStatus;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

public class PianoGame extends AbstractTimerGame implements TileLinesGame {


    private final String name;
    HashMap<ClickType, TileLine> lines = new HashMap<>();

    /**
     * use ms time
     * **/
    private final long totalTime;

    private final byte[] music;

    private final byte[] leftImage;

    private final byte[] centerImage;

    private final byte[] rightImage;

    public PianoGame(String name, long time, Map<ClickType, Tile[]> tiles, byte[] music, byte[] leftImage, byte[] centerImage, byte[] rightImage) {
        this.name = name;
        this.totalTime = time;
        this.music = music;
        this.leftImage = leftImage;
        this.centerImage = centerImage;
        this.rightImage = rightImage;
        for (Map.Entry<ClickType, Tile[]> entry : tiles.entrySet()) {
            try {
                lines.put(entry.getKey(), new TileLineContainer(this, Arrays.asList(entry.getValue())));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Illegal state on line "+ entry.getKey(), e);
            }
        }
    }

    @Override
    public void press(ClickType type, long globalTime) {
        TileLine line = lines.get(type);
        if (line != null)
            line.press(globalTime);
    }

    @Override
    public void release(ClickType type, long globalTime) {
        TileLine line = lines.get(type);
        if (line != null)
            line.release(globalTime);
    }

    @Override
    public String getName(String name) {
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
    public InputStream getLeftImage() {
        return new ByteArrayInputStream(leftImage);
    }

    @Override
    public InputStream getRightImage() {
        return new ByteArrayInputStream(rightImage);
    }

    @Override
    public InputStream getCenterImage() {
        return new ByteArrayInputStream(centerImage);
    }

    @Override
    public InputStream getMusic() {
        return new ByteArrayInputStream(music);
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

}
