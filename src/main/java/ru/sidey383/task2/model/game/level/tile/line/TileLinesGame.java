package ru.sidey383.task2.model.game.level.tile.line;

import ru.sidey383.task2.model.game.ClickType;
import ru.sidey383.task2.model.game.ClickableGame;
import ru.sidey383.task2.model.game.TimerGame;
import ru.sidey383.task2.model.game.level.tile.line.line.TileLine;

public interface TileLinesGame extends TimerGame, ClickableGame, TileScoreProvider {

    String getName();

    TileLine getLine(ClickType clickType);

    ClickType[] getAvailableTypes();

    /**
     * @return nano time
     * **/
    long getTimeToShow();



}
