package ru.sidey383.task2.view.choice;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import ru.sidey383.task2.event.EventManager;
import ru.sidey383.task2.view.choice.component.ChoicePane;
import ru.sidey383.task2.view.events.PlayerOpenMenuEvent;

import java.util.Collection;

public class GameChoiceAppScene extends ChoiceView {

    public VBox scrollBox;

    @Override
    public void setGameChoice(Collection<GameChoiceUnit> choices) {
        Platform.runLater(() -> {
                    scrollBox.getChildren().clear();
                    scrollBox.getChildren().addAll(choices.stream().map(ChoicePane::new).toList());
                }
        );
    }


    @FXML
    public void pressMenu() {
        EventManager.runEvent(new PlayerOpenMenuEvent());
    }

}
