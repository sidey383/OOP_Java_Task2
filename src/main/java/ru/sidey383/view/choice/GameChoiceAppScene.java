package ru.sidey383.view.choice;

import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import ru.sidey383.control.AvailableScene;
import ru.sidey383.event.EventHandler;
import ru.sidey383.event.EventManager;
import ru.sidey383.view.events.PlayerChangeSceneEvent;

import java.util.Collection;

public class GameChoiceAppScene extends ChoiceView {

    public VBox scrollBox;

    @Override
    public void setGameChoice(Collection<GameChoiceUnit> choices) {
        scrollBox.getChildren().addAll(choices.stream().map(ChoicePane::new).toList());
    }


    @EventHandler
    public void pressMenu(ActionEvent actionEvent) {
        EventManager.manager.runEvent(new PlayerChangeSceneEvent(AvailableScene.MENU));
    }
}
