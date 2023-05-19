package ru.sidey383.task2.view.game;

public interface GameRender {

    void updateTiles(DrawnTile[][] nTiles);

    void setTimeAdapter(TimeProvider adapter);

}
