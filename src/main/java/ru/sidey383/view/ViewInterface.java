package ru.sidey383.view;

public interface ViewInterface {

    public void setScene(SceneController scene);

    public <T extends SceneController> SceneControllerFactory<? extends T> getFactory(Class<T> clazz);

}
