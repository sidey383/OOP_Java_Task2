package ru.sidey383.task2.model.data.settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sidey383.task2.model.game.ClickType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class SettingsController implements SettingsProvider {

    private final Logger logger = LogManager.getLogger(SettingsController.class);

    private static final ObjectMapper mapper = new ObjectMapper();
    private AppSettings settings = null;

    private final Path settingsPath;

    public SettingsController(Path settingsPath) {
        this.settingsPath = settingsPath;
    }

    @Override
    public Map<ClickType, Integer> getGameKeys() {
        if (settings == null)
            readSetting();
        return settings.getGameKeys();
    }

    @Override
    public void setGameKey(ClickType type, int key) {
        if (settings == null)
            readSetting();
        settings.setGameKey(type, key);
        try {
            write();
        } catch (IOException e) {
            logger.error(() -> String.format("Settings write error %s", settingsPath), e);
        }
    }

    @Override
    public Path getGamesDir() {
        if (settings == null)
            readSetting();
        return settings.getGamesDir();
    }

    @Override
    public void setGamesDir(Path path) {
        if (settings == null)
            readSetting();
        settings.setGamesDir(path);
        try {
            write();
        } catch (IOException e) {
            logger.error(() -> String.format("Settings write error %s", path), e);
        }
    }

    private void readSetting() {
        if (!Files.exists(settingsPath)) {
            this.settings = AppSettings.getDefault(settingsPath.getParent());
            return;
        }
        try (InputStream is = Files.newInputStream(settingsPath)) {
            this.settings = mapper.reader().readValue(is, AppSettings.class);
        } catch (IOException e) {
            logger.warn("Settings read error", e);
            this.settings = AppSettings.getDefault(settingsPath.getParent());
        }
    }

    private void write() throws IOException {
        try (OutputStream os = Files.newOutputStream(settingsPath)) {
            mapper.writer().writeValue(os, settings);
        }
    }

}
