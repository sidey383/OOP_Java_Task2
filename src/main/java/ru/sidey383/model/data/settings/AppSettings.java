package ru.sidey383.model.data.settings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import ru.sidey383.model.game.ClickType;

import java.nio.file.Path;
import java.util.*;

public class AppSettings implements SettingsProvider {

    private final ClickKeys clickKeys;

    private Path gamePath;

    @JsonCreator
    public AppSettings(ClickKeys keys, Path gamePath) {
        this.clickKeys = keys;
        this.gamePath = gamePath;
    }

    public static AppSettings getDefault() {
        return new AppSettings(new AppSettings.ClickKeys(new Integer[]{0x41, 0x53, 0x44, 0x4A, 0x4B, 0x4C}), Path.of("games"));
    }

    @Override
    public synchronized Map<ClickType, Integer> getGameKeys() {
        return clickKeys.getMap();
    }

    @Override
    public synchronized void setGameKey(ClickType type, int key) {
        clickKeys.set(type, key);
    }

    @Override
    @JsonGetter(value = "gamePath")
    public synchronized Path getGamesDir() {
        return gamePath;
    }

    @Override
    public synchronized void setGamesDir(Path path) {
        this.gamePath = path;
    }

    @JsonGetter
    public ClickKeys getClickKeys() {
        return clickKeys;
    }

    private static class ClickKeys {

        Integer[] values;

        @JsonCreator
        public ClickKeys(Integer[] values) {
            if (values.length != 6)
                throw new IllegalArgumentException("Wrong values count");
            this.values = Arrays.copyOf(values, 6);
        }


        public int get(ClickType type) {
            return switch (type) {
                case LINE_1 -> values[0];
                case LINE_2 -> values[1];
                case LINE_3 -> values[2];
                case LINE_4 -> values[3];
                case LINE_5 -> values[4];
                case LINE_6 -> values[5];
            };
        }

        public void set(ClickType key, int value) {
            switch (key) {
                case LINE_1 -> values[0] = value;
                case LINE_2 -> values[1] = value;
                case LINE_3 -> values[2] = value;
                case LINE_4 -> values[3] = value;
                case LINE_5 -> values[4] = value;
                case LINE_6 -> values[5] = value;
            }
        }

        public Map<ClickType, Integer> getMap() {
            return Map.of(
                    ClickType.LINE_1, values[0],
                    ClickType.LINE_2, values[1],
                    ClickType.LINE_3, values[2],
                    ClickType.LINE_4, values[3],
                    ClickType.LINE_5, values[4],
                    ClickType.LINE_6, values[5]
                    );
        }

        @JsonGetter
        public Integer[] getValues() {
            return values;
        }

    }
}
