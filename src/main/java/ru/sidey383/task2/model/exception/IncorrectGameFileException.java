package ru.sidey383.task2.model.exception;

import ru.sidey383.task2.model.data.game.GameDescription;

public class IncorrectGameFileException extends ModelException {

    private final GameDescription gameDescription;

    public IncorrectGameFileException(GameDescription description, String message) {
        super(message);
        gameDescription = description;
    }
    public IncorrectGameFileException(GameDescription description, String message, Throwable throwable) {
        super(message, throwable);
        gameDescription = description;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + "\n" + gameDescription;
    }
}
