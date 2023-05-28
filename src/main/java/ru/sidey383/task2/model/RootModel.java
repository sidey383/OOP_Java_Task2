package ru.sidey383.task2.model;

import ru.sidey383.task2.event.EventManager;
import ru.sidey383.task2.model.data.DataController;
import ru.sidey383.task2.model.data.DataProvider;
import ru.sidey383.task2.model.data.game.GameDescription;
import ru.sidey383.task2.model.data.game.read.RawDataContainer;
import ru.sidey383.task2.model.data.game.read.ZIPGameReader;
import ru.sidey383.task2.model.data.score.GameScore;
import ru.sidey383.task2.model.data.settings.SettingsProvider;
import ru.sidey383.task2.model.event.ModelStartTileLinesGameEvent;
import ru.sidey383.task2.model.exception.IncorrectGameFileException;
import ru.sidey383.task2.model.exception.ModelException;
import ru.sidey383.task2.model.exception.ModelIOException;
import ru.sidey383.task2.model.game.level.PianoGame;
import ru.sidey383.task2.model.game.level.line.tile.TileStatus;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Collection;

public class RootModel implements ModelInterface {

    private final DataProvider dataProvider;

    private final ZIPGameReader gameReader = new ZIPGameReader();

    private RootModel(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public static RootModel createModel(Path dir) throws ModelException {
        return new RootModel(DataController.createController(dir));
    }

    @Override
    public void startGame(GameDescription gameDescription) throws ModelException {
        try {
            RawDataContainer dataContainer = gameReader.readZIP(gameDescription.gameContainer());

            if (!dataContainer.getHash().equals(gameDescription.hash())) {
                throw new IncorrectGameFileException(
                        gameDescription,
                        String.format("Wrong hash, expect %s value %s", gameDescription.hash(), dataContainer.getHash())
                );
            }

            PianoGame game = gameReader.readGame(dataContainer);

            game.addResultListener((data) -> {
                dataProvider.scoreProvider().addScore(gameDescription, data.stream().mapToLong(TileStatus::score).sum());
                return null;
            });

            EventManager.runEvent(new ModelStartTileLinesGameEvent(dataContainer, game));
        } catch (NoSuchFileException e) {
            throw new IncorrectGameFileException( gameDescription, "The file has been deleted", e);
        } catch (IOException e) {
            throw new ModelIOException(e);
        }
    }

    @Override
    public Collection<GameDescription> getGameDescriptions() {
        return dataProvider.gameProvider().readGameDescriptions();
    }

    @Override
    public Collection<GameScore> getScores() {
        return dataProvider.scoreProvider().getScores();
    }

    @Override
    public SettingsProvider getSettings() {
        return dataProvider.settingProvider();
    }
}
