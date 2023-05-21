package ru.sidey383.task2.model.data.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sidey383.task2.model.data.game.read.ZIPGameDescriptionReader;
import ru.sidey383.task2.model.data.settings.SettingsProvider;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.ZipException;

public class GameDataController implements GameProvider {

    private final static Logger logger = LogManager.getLogger(GameDataController.class);

    private final ZIPGameDescriptionReader gameDescriptionReader = new ZIPGameDescriptionReader();

    private final SettingsProvider settingsProvider;

    public GameDataController(SettingsProvider settings) {
        this.settingsProvider = settings;
    }

    @Override
    public Collection<GameDescription> readGameDescriptions() {
        Collection<GameDescription> descriptions = new ArrayList<>();
        try {
            createDefaultGames(settingsProvider.getGamesDir());
        } catch (IOException e) {
            logger.error(() -> String.format("Can't create default games at %s", settingsProvider.getGamesDir()), e);
        }
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(settingsProvider.getGamesDir())) {
            ds.forEach((path) -> {
                if (!Files.isRegularFile(path))
                    return;
                try {
                    descriptions.add(
                            gameDescriptionReader.readDescription(path)
                    );
                } catch (ZipException e) {
                    logger.debug(() -> String.format("Wrong file in games folder %s", path), e);
                } catch (Exception e) {
                    logger.warn(() -> String.format("Game description read error %s", path), e);
                }
            });
        } catch (Exception e) {
            logger.error(() -> String.format("Error reading the games folder %s", settingsProvider.getGamesDir()), e);
        }

        return descriptions;
    }

    private void createDefaultGames(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
            Path game = path.resolve("exampleGame.zip");
            try (InputStream is = getClass().getResourceAsStream("/game/exampleGame.zip")) {
                assert is != null;
                Files.copy(is, game);
            }
        }
    }

}
