package ru.sidey383.task2.control.exception;

import ru.sidey383.task2.control.ControllerModule;

public class ModuleLiveCycleException extends ControllerException {

    public ModuleLiveCycleException(String message, ControllerModule module) {
        super(module + " : " + message);
    }

}
