package ru.sidey383.view.game;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import ru.sidey383.view.SceneController;

import java.net.URL;
import java.util.ResourceBundle;

public class GameSceneController extends SceneController implements Initializable {

    @FXML
    private Label keyLabel;

    @FXML
    private Canvas gameField;

    @FXML
    private ImageView background;

    @FXML
    public void mouseClick(MouseEvent mouseEvent) {
        System.out.println(mouseEvent);
        keyLabel.setText("Mouse event" + mouseEvent);
    }

    @FXML
    public void onKeyPress(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.UNDEFINED)
            return;
        System.out.println(keyEvent);
        keyLabel.setText("Press: " + keyEvent.getCode());
    }

    @FXML
    public void onKeyRelease(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.UNDEFINED)
            return;
        System.out.println(keyEvent);
        keyLabel.setText("Release: " + keyEvent.getCode());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            background.setImage(new Image("/background.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTileInfo() {

    }

}
