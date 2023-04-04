package ru.sidey383.view;

public interface ViewInterface {

    void setScene(SceneController scene);

    <T extends SceneController> SceneControllerFactory<? extends T> getFactory(Class<T> clazz);

    void close();

}
