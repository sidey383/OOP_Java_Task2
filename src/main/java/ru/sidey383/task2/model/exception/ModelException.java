package ru.sidey383.task2.model.exception;

public abstract class ModelException extends Exception {

    public ModelException() {}

    public ModelException(Throwable cause) {
        super(cause);
    }

    public ModelException(String message) {
        super(message);
    }

    public ModelException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
