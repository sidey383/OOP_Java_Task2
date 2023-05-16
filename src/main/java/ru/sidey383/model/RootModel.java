package ru.sidey383.model;

import ru.sidey383.event.EventManager;
import ru.sidey383.model.data.DataProvider;
import ru.sidey383.model.data.game.GameDescription;
import ru.sidey383.model.data.game.read.RawDataContainer;
import ru.sidey383.model.data.game.read.ZIPGameReader;
import ru.sidey383.model.data.score.GameScore;
import ru.sidey383.model.data.settings.SettingsProvider;
import ru.sidey383.model.event.ModelStartTileLinesGameEvent;
import ru.sidey383.model.exception.ModelException;
import ru.sidey383.model.exception.ModelIOException;
import ru.sidey383.model.game.TileLinesGame;

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
        return new RootModel(DataProvider.createDataProvider(dir));
    }

    @Override
    public void startGame(GameDescription gameDescription) throws ModelException {
        try {
            RawDataContainer dataContainer = gameReader.readZIP(gameDescription.getGameContainer());
            TileLinesGame game = gameReader.readGame(dataContainer);
            EventManager.manager.runEvent(new ModelStartTileLinesGameEvent(dataContainer, game));
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
