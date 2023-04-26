package ru.sidey383.model.event;

import ru.sidey383.model.game.TileLinesGame;
import ru.sidey383.model.game.read.DataContainer;

public class ModelStartTileLinesGameEvent extends ModelStartGameEvent {

    private final TileLinesGame game;

    public ModelStartTileLinesGameEvent(DataContainer dataContainer, TileLinesGame game) {
        super(dataContainer);
        this.game = game;
    }

    public TileLinesGame getGame() {
        return game;
    }
}
