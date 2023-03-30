package ru.sidey383.view.game;


import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import ru.sidey383.view.StageController;
import ru.sidey383.view.RootNode;

public class GameController extends RootNode {

    @FXML
    private Label keyLabel;

    @FXML
    private Canvas gameField;

    public GameController(StageController controller) {
        super(controller);
    }

    @FXML
    public void mouseClick(MouseEvent mouseEvent) {
        System.out.println(mouseEvent);
        keyLabel.setText("Mouse event" + mouseEvent);
    }

    public void onKeyPress(KeyEvent keyEvent) {
        System.out.println(keyEvent);
        keyLabel.setText("Press: " + keyEvent.getText());
    }

    public void onKeyRelease(KeyEvent keyEvent) {
        System.out.println(keyEvent);
        keyLabel.setText("Release: " + keyEvent.getText());
    }
}
