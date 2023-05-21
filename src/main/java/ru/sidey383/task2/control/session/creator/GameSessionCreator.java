package ru.sidey383.task2.control.session.creator;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sidey383.task2.control.ControllerSessionCreator;
import ru.sidey383.task2.control.session.GameChoiceSession;
import ru.sidey383.task2.control.session.GameSession;
import ru.sidey383.task2.event.EventHandler;
import ru.sidey383.task2.event.GameChoiceEvent;
import ru.sidey383.task2.model.data.game.read.RawDataContainer;
import ru.sidey383.task2.model.event.ModelStartTileLinesGameEvent;
import ru.sidey383.task2.model.exception.IncorrectGameFileException;
import ru.sidey383.task2.model.exception.ModelException;
import ru.sidey383.task2.view.game.GameView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class GameSessionCreator extends ControllerSessionCreator {

    private final Logger logger = LogManager.getLogger(GameSessionCreator.class);

    private final GameChoiceSession gameChoiceSession;

    public GameSessionCreator(GameChoiceSession gameChoiceSession) {
        this.gameChoiceSession = gameChoiceSession;
    }

    @EventHandler
    public void onGameChoice(GameChoiceEvent e) {
        try {
            controller().getModel().startGame(e.getGameDescription());
        } catch (IncorrectGameFileException ex) {
            logger.warn("The file may have been modified or removed", ex);
            gameChoiceSession.updateDescriptions();
        } catch (ModelException ex) {
            logger.fatal("Game start error", ex);
        }
    }

    @EventHandler
    public void onTileLineGameStart(ModelStartTileLinesGameEvent event) {
        try {
            GameView gameView = controller().getScene(GameView.class);
            if (gameView == null) {
                logger.error("Can't create game view");
                return;
            }
            setGameStyle(gameView, event.getData());
            controller().setSession(new GameSession(event.getGame(), gameView));
        } catch (Exception e) {
            logger.fatal("Game scene create error", e);
        }
    }

    private void setGameStyle(GameView gameView, RawDataContainer container) throws IOException {
        gameView.setRightImage(new Image(new ByteArrayInputStream(
                container.getData(byte[].class, "right")
                        .orElse(new byte[0])
        )));
        gameView.setLeftImage(new Image(new ByteArrayInputStream(
                container.getData(byte[].class, "left")
                        .orElse(new byte[0])
        )));
        gameView.setCenterImage(new Image(new ByteArrayInputStream(
                container.getData(byte[].class, "center")
                        .orElse(new byte[0])
        )));
        Path tempMusicPath = Files.createTempFile("gameMedia", "");
        try (OutputStream os = Files.newOutputStream(tempMusicPath)) {
            os.write(container
                    .getData(byte[].class, "music")
                    .orElse(new byte[0]));
        } catch (IOException e) {
            logger.error(() -> String.format("Music write error %s", tempMusicPath), e);
        }
        gameView.setMusic(new Media(tempMusicPath.toUri().toString()));
    }

}
