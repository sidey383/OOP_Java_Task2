package ru.sidey383.task2;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.sidey383.task2.control.Controller;
import ru.sidey383.task2.control.session.creator.GameChoiceSessionCreator;
import ru.sidey383.task2.control.session.creator.MenuSessionCreator;
import ru.sidey383.task2.control.session.creator.ScoreSessionCreator;
import ru.sidey383.task2.event.EventManager;
import ru.sidey383.task2.model.RootModel;
import ru.sidey383.task2.view.View;
import ru.sidey383.task2.view.events.PlayerOpenMenuEvent;

import java.nio.file.Path;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        View view = new View(primaryStage);
        RootModel model = RootModel.createModel(Path.of("."));
        Controller controller = new Controller(view, model);
        controller.addSessionCreator(new MenuSessionCreator());
        controller.addSessionCreator(new ScoreSessionCreator());
        controller.addSessionCreator(new GameChoiceSessionCreator());
        EventManager.runEvent(new PlayerOpenMenuEvent());
    }

}