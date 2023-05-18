package ru.sidey383.task2.view.score.component;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import ru.sidey383.task2.view.score.ScoreUnit;

public class ScoreBox extends HBox {

    public ScoreBox(ScoreUnit unit) {
        Label textLabel = new Label(unit.getGameName());
        Label scoreLabel = new Label(Long.toString(unit.getScore()));
        getChildren().add(textLabel);
        getChildren().add(scoreLabel);
    }

}
