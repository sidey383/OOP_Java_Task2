package ru.sidey383.model.game;

import ru.sidey383.model.intarface.ClickType;
import ru.sidey383.model.intarface.TileLine;
import ru.sidey383.model.intarface.TileLinesGame;
import ru.sidey383.model.intarface.tile.Tile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PianoGame extends AbstractTimerGame implements TileLinesGame {

    HashMap<ClickType, TileLine> lines;

    public PianoGame(Map<ClickType, Tile[]> tiles) {
        for (Map.Entry<ClickType, Tile[]> entry : tiles.entrySet()) {
            lines.put(entry.getKey(), new TileLineContainer(this, Arrays.asList(entry.getValue())));
        }
    }

    @Override
    public void press(ClickType type) {
        lines.get(type).press();
    }

    @Override
    public void release(ClickType type) {
        lines.get(type).press();
    }

    @Override
    public void stop() {
        lines.values().forEach(TileLine::onEnd);
    }

    @Override
    public TileLine getLine(ClickType clickType) {
        return lines.get(clickType);
    }

    @Override
    public ClickType[] getAvailableTypes() {
        return new ClickType[] {ClickType.LEFT_1, ClickType.LEFT_2, ClickType.RIGHT_1, ClickType.RIGHT_2};
    }
}
