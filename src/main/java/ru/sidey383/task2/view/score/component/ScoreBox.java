package ru.sidey383.task2.view.score.component;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import ru.sidey383.task2.view.score.ScoreUnit;

public class ScoreBox extends HBox {

    public ScoreBox(ScoreUnit unit) {
        Label textLabel = new Label(unit.name());
        Label scoreLabel = new Label(Long.toString(unit.score()));
        getChildren().add(textLabel);
        getChildren().add(scoreLabel);
    }

}
