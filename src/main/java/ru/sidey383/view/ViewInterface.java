package ru.sidey383.view;

import java.io.IOException;

public interface ViewInterface {

    void setScene(Scene scene);

    <T extends Scene> T getScene(Class<T> clazz) throws IOException;

    void close();

}
