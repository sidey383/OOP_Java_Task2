package ru.sidey383.task2.model.event;

import ru.sidey383.task2.event.Event;
import ru.sidey383.task2.model.data.game.read.RawDataContainer;

public class ModelStartGameEvent extends Event {

    private final RawDataContainer container;

    public ModelStartGameEvent(RawDataContainer source) {
        super(true);
        this.container = source;
    }

    public RawDataContainer getData() {
        return container;
    }

}
