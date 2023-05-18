package ru.sidey383.task2.view.game;

import ru.sidey383.task2.control.TimeAdapter;
import ru.sidey383.task2.model.game.level.line.tile.Tile;

public interface GameRender {

    public void updateTiles(Tile[][] nTiles);

    public void setTimeAdapter(TimeAdapter adapter);

}
