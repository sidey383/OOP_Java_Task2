package ru.sidey383.model.event;

import ru.sidey383.event.Event;
import ru.sidey383.model.game.read.DataContainer;

public class ModelStartGameEvent extends Event {

    private final DataContainer container;

    public ModelStartGameEvent(DataContainer source) {
        super(true);
        this.container = source;
    }

    public DataContainer getData() {
        return container;
    }

}
