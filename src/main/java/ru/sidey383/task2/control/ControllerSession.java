package ru.sidey383.task2.control;

import ru.sidey383.task2.event.EventManager;
import ru.sidey383.task2.view.AppScene;

public abstract class ControllerSession {

    private final Controller controller;

    public ControllerSession(Controller controller) {
        this.controller = controller;
    }

    public void start() {
        EventManager.registerListener(this);
        getController().getView().setScene(getScene());
    }

    public void end() {
        EventManager.unregisterListener(this);
    }

    public Controller getController() {
        return controller;
    }

    public abstract AppScene getScene();

}
