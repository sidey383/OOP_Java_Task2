package ru.sidey383.view.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import ru.sidey383.view.StageController;
import ru.sidey383.view.RootNode;

public class MenuController extends RootNode {

    public Label mainTitle;

    @FXML
    private Button startButton;

    @FXML
    private Button scoreButton;

    public MenuController(StageController controller) {
        super(controller);
    }

    @FXML
    private void startGame() {
        stageController.getStage().setScene(stageController.getGameStartScene());
        stageController.getStage().show();
    }


    @FXML
    public void showScore(ActionEvent actionEvent) {
    }
}
