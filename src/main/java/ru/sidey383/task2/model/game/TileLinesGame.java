package ru.sidey383.task2.model.game;

import ru.sidey383.task2.model.game.level.line.TileLine;
import ru.sidey383.task2.model.game.level.line.tile.TileStatus;

import java.util.Collection;

public interface TileLinesGame extends TimerGame, ClickableGame {

    String getName();

    TileLine getLine(ClickType clickType);

    ClickType[] getAvailableTypes();

    /**
     * @return nano time
     * **/
    long getTimeToShow();
    Collection<TileStatus> getStatistic();

}
