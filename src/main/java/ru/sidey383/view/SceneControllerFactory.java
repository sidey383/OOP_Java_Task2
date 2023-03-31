package ru.sidey383.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;

public abstract class SceneControllerFactory<T extends SceneController> {

    public T createScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getFXMLPath());
        loader.setControllerFactory(this::controllerFXMLFactory);
        Scene scene = new Scene(loader.load());
        T sceneController = loader.getController();
        sceneController.setScene(scene);
        return sceneController;
    }

    public abstract boolean canProduceType(Class<? extends SceneController> clazz);

    protected abstract URL getFXMLPath();

    protected abstract Object controllerFXMLFactory(Class<?> clazz);


}
