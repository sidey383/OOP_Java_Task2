package ru.sidey383.view.events;

import ru.sidey383.control.AvailableScene;
import ru.sidey383.event.Event;

public class PlayerChangeSceneEvent extends Event {

    private final AvailableScene scene;

    public PlayerChangeSceneEvent(AvailableScene scene) {
        super(false);
        this.scene = scene;
    }

    public AvailableScene getScene() {
        return scene;
    }

}
