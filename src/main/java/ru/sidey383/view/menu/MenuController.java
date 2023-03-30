package ru.sidey383.view.menu;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class MenuController {

    public Label mainTitle;

    public void startGame(MouseEvent mouseEvent) {
        System.out.println("Start game "+ mouseEvent.toString());
    }

    public void showScore(MouseEvent mouseEvent) {
        System.out.println("Show score "+ mouseEvent.toString());
    }
}
