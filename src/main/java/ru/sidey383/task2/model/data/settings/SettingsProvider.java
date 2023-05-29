package ru.sidey383.task2.model.data.settings;

import ru.sidey383.task2.model.game.ClickType;

import java.nio.file.Path;
import java.util.Map;

public interface SettingsProvider {

    Map<ClickType, Integer> getGameKeys();

    void setGameKey(ClickType type, int key);

    Path getGamesDir();

    void setGamesDir(Path path);

}
