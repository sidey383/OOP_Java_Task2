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
import ru.sidey383.task2.model.exception.ModelException;
import ru.sidey383.task2.model.exception.ModelIOException;
import ru.sidey383.task2.model.game.level.tile.line.PianoGame;
import ru.sidey383.task2.model.game.level.tile.line.line.tile.TileStatus;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
public class RootModel implements ModelInterface {

    DataProvider dataProvider;

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
            RawDataContainer dataContainer = gameReader.readZIP(gameDescription.getGameContainer());
            PianoGame game = gameReader.readGame(dataContainer);
            game.addResultListener((data) -> {
                dataProvider.getScoreProvider().addScore(gameDescription, data.stream().mapToLong(TileStatus::getScore).sum());
                return null;
            });
            EventManager.runEvent(new ModelStartTileLinesGameEvent(dataContainer, game));
        } catch (IOException e) {
            throw new ModelIOException(e);
        }
    }

    @Override
    public Collection<GameDescription> getGameDescriptions() {
        return dataProvider.getGameProvider().getGames();
    }

    @Override
    public Collection<GameScore> getScores() {
        return dataProvider.getScoreProvider().getScores();
    }

    @Override
    public SettingsProvider getSettings() {
        return dataProvider.getSettingProvider();
    }
}
