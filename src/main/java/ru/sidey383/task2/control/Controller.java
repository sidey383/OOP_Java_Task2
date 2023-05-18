package ru.sidey383.task2.control;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sidey383.task2.control.session.ChoiceSession;
import ru.sidey383.task2.control.session.GameSession;
import ru.sidey383.task2.control.session.MenuSession;
import ru.sidey383.task2.control.session.ScoreSession;
import ru.sidey383.task2.event.EventHandler;
import ru.sidey383.task2.event.EventManager;
import ru.sidey383.task2.model.ModelInterface;
import ru.sidey383.task2.model.event.ModelStartTileLinesGameEvent;
import ru.sidey383.task2.model.game.TileLinesGame;
import ru.sidey383.task2.model.data.game.read.RawDataContainer;
import ru.sidey383.task2.model.data.settings.SettingsProvider;
import ru.sidey383.task2.view.ViewInterface;
import ru.sidey383.task2.view.choice.ChoiceView;
import ru.sidey383.task2.view.events.PlayerChangeSceneEvent;
import ru.sidey383.task2.view.events.WindowCloseEvent;
import ru.sidey383.task2.view.game.GameView;
import ru.sidey383.task2.view.menu.MenuView;
import ru.sidey383.task2.view.score.ScoreView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Controller {

    private static final Logger logger = LogManager.getLogger(Controller.class);

    private final ViewInterface view;

    private final SettingsProvider settings;

    private final ModelInterface model;

    private ControllerSession session = null;

    public Controller(ViewInterface view, ModelInterface model) {
        this.view = view;
        this.settings = model.getSettings();
        this.model = model;
        EventManager.manager.registerListener(this);
    }

    public void openGame(RawDataContainer container, TileLinesGame game) {
        GameView gameView;

        try {
            gameView = view.getScene(GameView.class);
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
            Path tempMusicPath =  Files.createTempFile("gameMedia", "");
            //TODO: remove temp file
            try (OutputStream os = Files.newOutputStream(tempMusicPath)) {
                os.write(container
                        .getData(byte[].class, "music")
                        .orElse(new byte[0]));
            } catch (IOException e) {
                logger.error(() -> String.format("Music write error %s", tempMusicPath), e);
            }
            gameView.setMusic(new Media(tempMusicPath.toUri().toString()));
            view.setScene(gameView);

        } catch (IOException e) {
            logger.error("Game scene create error", e);
            gameView = null;
        }

        if (gameView == null)
            return;

        setSession(new GameSession(this, game, gameView));
    }

    public void openMenu() {
        MenuView menuView = null;
        try {
            menuView = view.getScene(MenuView.class);
        } catch (IOException e) {
            logger.fatal("Menu scene score scene create error", e);
        }
        if (menuView == null)
            return;
        view.setScene(menuView);
        setSession(new MenuSession(this, menuView));
    }

    public void openGameChoice() {
        try {
            ChoiceView choiceView = view.getScene(ChoiceView.class);
            view.setScene(choiceView);
            setSession(new ChoiceSession(this, choiceView, getModel().getGameDescriptions()));
        } catch (Exception e) {
            logger.fatal("Menu scene score scene create error", e);
        }
    }

    public void openGameScore() {
        try {
            ScoreView choiceView = view.getScene(ScoreView.class);
            view.setScene(choiceView);
            setSession(new ScoreSession(this, choiceView, getModel().getScores()));
        } catch (Exception e) {
            logger.error("Game score scene create error", e);
            e.printStackTrace();
        }
    }

    private void setSession(ControllerSession newSession) {
        if (session != null)
            session.end();
        session = newSession;
        session.start();
    }

    @EventHandler
    public void onWindowsClose(WindowCloseEvent e) {
        if (session != null) {
            session.end();
        }
    }

    @EventHandler
    public void onPlayerOpenScene(PlayerChangeSceneEvent e) {
        switch (e.getScene()) {
            case MENU -> openMenu();
            case GAME_CHOOSE -> openGameChoice();
            case SCORE -> openGameScore();
            default -> {}
        }
    }

    @EventHandler
    public void onTileLineGameStart(ModelStartTileLinesGameEvent event) {
        openGame(event.getData(), event.getGame());
    }

    public ControllerSession getSession() {
        return session;
    }

    public SettingsProvider getSettings() {
        return settings;
    }

    public ViewInterface getView() {
        return view;
    }

    public ModelInterface getModel() {
        return model;
    }

    public void stop() {
        view.close();
        if (session != null) {
            session.end();
        }
        EventManager.manager.unregisterListener(this);
    }

}
