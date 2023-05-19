package ru.sidey383.task2.model.event;

import ru.sidey383.task2.model.game.level.tile.line.TileLinesGame;
import ru.sidey383.task2.model.data.game.read.RawDataContainer;

public class ModelStartTileLinesGameEvent extends ModelStartGameEvent {

    private final TileLinesGame game;

    public ModelStartTileLinesGameEvent(RawDataContainer rawDataContainer, TileLinesGame game) {
        super(rawDataContainer);
        this.game = game;
    }

    public TileLinesGame getGame() {
        return game;
    }
}
