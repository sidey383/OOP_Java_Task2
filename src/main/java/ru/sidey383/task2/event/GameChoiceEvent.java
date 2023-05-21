package ru.sidey383.task2.event;

import ru.sidey383.task2.model.data.game.GameDescription;

public class GameChoiceEvent extends Event {

    private final GameDescription gameDescription;

    public GameChoiceEvent(GameDescription gameDescription) {
        super(true);
        this.gameDescription = gameDescription;
    }

    public GameDescription getGameDescription() {
        return gameDescription;
    }

}
