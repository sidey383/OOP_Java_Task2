package ru.sidey383.task2.view.game;

public interface GameRender {

    void updateView(DrawnInfo[][] nTiles, LineStatus[] status);

    void setTimeAdapter(TimeProvider adapter);

}
