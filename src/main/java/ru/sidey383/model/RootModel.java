package ru.sidey383.model;

import javafx.scene.input.KeyCode;
import ru.sidey383.event.EventManager;
import ru.sidey383.model.event.ModelStartTileLinesGameEvent;
import ru.sidey383.model.game.ClickType;
import ru.sidey383.model.game.description.GameDescription;
import ru.sidey383.model.game.level.PianoGame;
import ru.sidey383.model.game.read.PianoGameReader;
import ru.sidey383.model.score.GameScore;
import ru.sidey383.model.settings.AppSettings;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class RootModel implements ModelInterface {

    PianoGameReader reader = new PianoGameReader();

    @Override
    public void startGame(GameDescription gameDescription) throws Exception {
        PianoGame game = reader.readGame(gameDescription.getGameContainer());
        EventManager.manager.runEvent(new ModelStartTileLinesGameEvent(game));
    }

    @Override
    public Collection<GameDescription> getGameDescriptions() {
        Collection<GameDescription> descriptions = new ArrayList<>();
        Path gamesPath = getSettings().gamesPath();
        try(DirectoryStream<Path> ds = Files.newDirectoryStream(gamesPath)) {
            ds.forEach((path) -> {
                try {
                    reader.readGameDescription(path.toUri().toURL()).ifPresent(descriptions::add);
                } catch (Exception e) {
                    //TODO: come logging
                }
            });
        } catch (Exception e) {
            //TODO: come logging
        }
        return descriptions;
    }

    @Override
    public Collection<GameScore> getScores() {
        return null;
    }

    @Override
    public AppSettings getSettings() {
        return new AppSettings() {
            @Override
            public Map<ClickType, Integer> gameKeys() {
                return Map.of(
                        ClickType.LINE_1, KeyCode.A.getCode(),
                        ClickType.LINE_2, KeyCode.S.getCode(),
                        ClickType.LINE_3, KeyCode.D.getCode(),
                        ClickType.LINE_4, KeyCode.J.getCode(),
                        ClickType.LINE_5, KeyCode.K.getCode(),
                        ClickType.LINE_6, KeyCode.L.getCode()
                        );
            }

            @Override
            public Path gamesPath() {
                return Paths.get("");
            }
        };
    }
}
