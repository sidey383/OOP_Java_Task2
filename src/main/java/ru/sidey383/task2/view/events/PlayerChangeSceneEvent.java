package ru.sidey383.task2.view.events;

import ru.sidey383.task2.event.Event;

public class PlayerChangeSceneEvent extends Event {

    private final AvailableScene scene;

    public PlayerChangeSceneEvent(AvailableScene scene) {
        super(false);
        this.scene = scene;
    }

    public AvailableScene getScene() {
        return scene;
    }

    public enum AvailableScene {
        MENU, GAME, SCORE, GAME_CHOOSE
    }


}
