package ru.sidey383.model.game;

import ru.sidey383.model.game.level.line.TileLine;
import ru.sidey383.model.game.level.line.tile.TileStatus;

import java.io.InputStream;
import java.util.Collection;

public interface TileLinesGame extends TimerGame, ClickableGame {

    String getName(String name);

    TileLine getLine(ClickType clickType);

    ClickType[] getAvailableTypes();

    /**
     * @return nano time
     * **/
    long getTimeToShow();

    public Collection<TileStatus> getStatistic();

}
