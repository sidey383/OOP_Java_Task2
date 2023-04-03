package ru.sidey383.model.settings;

import ru.sidey383.model.game.ClickType;

import java.util.Map;

public interface AppSettings {

    public Map<ClickType, Integer> gameKeys();

}
