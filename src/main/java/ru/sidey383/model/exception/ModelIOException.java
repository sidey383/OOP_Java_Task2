package ru.sidey383.model.exception;

import java.io.IOException;

public class ModelIOException extends ModelException {

    public ModelIOException(IOException ex) {
        super(ex);
    }

    @Override
    public String getMessage() {
        return "IOException in model";
    }
}
