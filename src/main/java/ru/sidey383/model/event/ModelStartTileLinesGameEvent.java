package ru.sidey383.model.event;

import ru.sidey383.model.game.TileLinesGame;
import ru.sidey383.model.data.game.read.RawDataContainer;

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
