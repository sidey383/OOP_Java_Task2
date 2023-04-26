package ru.sidey383.view.choice;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ChoicePane extends HBox {

    private Label textLabel;

    private Button button;

    public ChoicePane(GameChoiceUnit gameChoiceUnit) {
        this.textLabel = new Label(gameChoiceUnit.getText());
        this.button = new Button("Start");
        getChildren().add(this.textLabel);
        getChildren().add(this.button);
    }

}
