package ru.sidey383.model.event;

import ru.sidey383.event.Event;
import ru.sidey383.model.game.TileLinesGame;

public class ModelStartTileLinesGameEvent extends Event {

    private final TileLinesGame game;

    public ModelStartTileLinesGameEvent(TileLinesGame game) {
        super(true);
        this.game = game;
    }

    public TileLinesGame getGame() {
        return game;
    }
}
