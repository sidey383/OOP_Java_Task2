package ru.sidey383.model.data.game;

import ru.sidey383.model.data.game.read.ZIPGameDescriptionReader;
import ru.sidey383.model.data.settings.SettingsProvider;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.ZipException;

public class GameContainer implements GameProvider {

    private final ZIPGameDescriptionReader gameDescriptionReader = new ZIPGameDescriptionReader();

    private final SettingsProvider settingsProvider;

    public GameContainer(SettingsProvider settings) {
        this.settingsProvider = settings;
    }

    @Override
    public Collection<GameDescription> getGames() {
        Collection<GameDescription> descriptions = new ArrayList<>();
        try {
            createDefaultGames(settingsProvider.getGamesDir());
        } catch (IOException e) {
            //TODO: logging
        }
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(settingsProvider.getGamesDir())) {
            ds.forEach((path) -> {
                if (!Files.isRegularFile(path))
                    return;
                try {
                    descriptions.add(
                            gameDescriptionReader.readDescription(path.toUri().toURL())
                    );
                } catch (ZipException e) {
                    //It's not correct zip file, ignore this?
                } catch (Exception e) {
                    //TODO: some logging
                }
            });
        } catch (Exception e) {
            //TODO: some logging
        }

        return descriptions;
    }

    private void createDefaultGames(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
            Path game = path.resolve("exampleGame.zip");
            try (InputStream is = getClass().getResourceAsStream("/exampleGame.zip")) {
                assert is != null;
                Files.copy(is, game);
            }
        }
    }

}
