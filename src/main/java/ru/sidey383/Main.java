package ru.sidey383;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.sidey383.view.View;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        new View(primaryStage);
    }
}