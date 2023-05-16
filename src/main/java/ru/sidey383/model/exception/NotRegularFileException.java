package ru.sidey383.model.exception;

import java.nio.file.Path;

public class NotRegularFileException extends ModelException {

    private final Path path;

    public NotRegularFileException(Path path) {
        this.path = path;
    }

    public NotRegularFileException(Path path, Throwable cause) {
        super(cause);
        this.path = path;
    }

    @Override
    public String getMessage() {
        return "File exists, but it's not regular file: " + path;
    }
}
