package ru.sidey383.model.data.settings;

import ru.sidey383.model.game.ClickType;

import java.nio.file.Path;
import java.util.Map;

public interface SettingsProvider {

    public Map<ClickType, Integer> getGameKeys();

    public void setGameKey(ClickType type, int key);

    public Path getGamesDir();

    public void setGamesDir(Path path);

}
