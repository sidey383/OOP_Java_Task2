package ru.sidey383.view.game;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import ru.sidey383.view.AppScene;

public abstract class GameView extends AppScene implements GameRender {

    public abstract void start();

    public abstract void stop();

    public abstract void showScore(String scoreStr);

    public abstract void setLeftImage(Image image);

    public abstract void setRightImage(Image image);

    public abstract void setCenterImage(Image image);

    public abstract void setMusic(Media sound);

}
