package ru.sidey383.view.game;

import ru.sidey383.view.SceneController;

public abstract class GameView extends SceneController implements GameRender {

    public abstract void startRender();

    public abstract void stopRender();

    public abstract void showScore();

}
