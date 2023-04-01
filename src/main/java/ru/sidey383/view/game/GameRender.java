package ru.sidey383.view.game;

import ru.sidey383.control.TimeAdapter;
import ru.sidey383.model.game.tile.Tile;

public interface GameRender {

    public void updateTiles(Tile[][] nTiles);

    public void setTimeAdapter(TimeAdapter adapter);

}
