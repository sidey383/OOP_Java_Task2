package ru.sidey383.model.game;

import ru.sidey383.event.Event;

public class ModelGameStartEvent extends Event {

    public ModelGameStartEvent(GameLore gameLore) {
        super(true);
    }
}
