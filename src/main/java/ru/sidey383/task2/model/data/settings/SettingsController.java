package ru.sidey383.task2.model.data.settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sidey383.task2.model.exception.ModelException;
import ru.sidey383.task2.model.exception.ModelIOException;
import ru.sidey383.task2.model.exception.NotRegularFileException;
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
    private final AppSettings settings;
    private final Path path;

    private SettingsController(AppSettings settings, Path path) {
        this.settings = settings;
        this.path = path;
    }

    @Override
    public Map<ClickType, Integer> getGameKeys() {
        return settings.getGameKeys();
    }

    @Override
    public void setGameKey(ClickType type, int key) {
        settings.setGameKey(type, key);
        try {
            write();
        } catch (IOException e) {
            logger.error(() -> String.format("Settings write error %s", path), e);
        }
    }

    @Override
    public Path getGamesDir() {
        return settings.getGamesDir();
    }

    @Override
    public void setGamesDir(Path path) {
        settings.setGamesDir(path);
        try {
            write();
        } catch (IOException e) {
            logger.error(() -> String.format("Settings write error %s", path), e);
        }
    }

    public static SettingsController createSettingsController(Path path) throws ModelException {
        SettingsController settingsContainer;
        if (Files.exists(path)) {
            if (Files.isRegularFile(path)) {
                try (InputStream is = Files.newInputStream(path)) {
                    AppSettings settings = mapper.reader().readValue(is, AppSettings.class);
                    settingsContainer = new SettingsController(settings, path);
                } catch (IOException e) {
                    throw new ModelIOException(e);
                }
            } else {
                throw new NotRegularFileException(path);
            }
        } else {
            try {
                return createNew(path);
            } catch (IOException e) {
                throw new ModelIOException(e);
            }
        }
        return settingsContainer;
    }

    private static SettingsController createNew(Path path) throws IOException {
        if (Files.exists(path))
            Files.delete(path);
        AppSettings settings = AppSettings.getDefault();
        SettingsController settingsController = new SettingsController(settings, path);
        settingsController.write();
        return settingsController;
    }

    private void write() throws IOException {
        try (OutputStream os = Files.newOutputStream(path)) {
            mapper.writer().writeValue(os, settings);
        }
    }

}
