package ru.sidey383.task2.view.choice.component;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import ru.sidey383.task2.view.choice.GameChoiceUnit;

public class ChoicePane extends HBox {

    public ChoicePane(GameChoiceUnit gameChoiceUnit) {
        Label textLabel = new Label(gameChoiceUnit.getText());
        Button button = new Button("Start");
        button.setOnMouseClicked(e -> gameChoiceUnit.apply());
        getChildren().add(textLabel);
        getChildren().add(button);
    }

}
