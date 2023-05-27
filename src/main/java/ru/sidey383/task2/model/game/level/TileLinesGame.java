package ru.sidey383.task2.model.game.level;

import ru.sidey383.task2.model.game.ClickType;
import ru.sidey383.task2.model.game.ClickableGame;
import ru.sidey383.task2.model.game.TimerGame;
import ru.sidey383.task2.model.game.level.line.TileLine;

public interface TileLinesGame extends TimerGame, ClickableGame, TileScoreProvider {

    String name();

    TileLine getLine(ClickType clickType);

    ClickType[] availableClickTypes();

    /**
     * @return nano time
     * **/
    long getTimeToShow();



}
