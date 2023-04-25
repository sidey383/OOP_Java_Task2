package ru.sidey383.view;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;

public abstract class SceneFactory<T extends Scene> {

    public T createScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getFXMLPath());
        loader.setControllerFactory(this::controllerFXMLFactory);
        javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());
        T sceneController = loader.getController();
        sceneController.setScene(scene);
        return sceneController;
    }

    public abstract boolean canProduceType(Class<? extends Scene> clazz);

    protected abstract URL getFXMLPath();

    protected abstract Object controllerFXMLFactory(Class<?> clazz);


}
