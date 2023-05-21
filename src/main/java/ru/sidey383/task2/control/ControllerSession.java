package ru.sidey383.task2.control;

import ru.sidey383.task2.view.AppScene;


/**
 * Controller session module. Manages a separate scene.
 * **/
public abstract class ControllerSession extends ControllerModule {

    public abstract AppScene getScene();

}
