package ru.sidey383.task2.control;

import ru.sidey383.task2.event.EventManager;

public abstract class ControllerSession {

    private final Controller controller;

    public ControllerSession(Controller controller) {
        this.controller = controller;
    }

    public void start() {
        EventManager.manager.registerListener(this);
    }

    public void end() {
        EventManager.manager.unregisterListener(this);
    }

    public Controller getController() {
        return controller;
    }

}
