package ru.sidey383.task2.control;

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
import ru.sidey383.task2.view.events.PlayerChangeSceneEvent;
import ru.sidey383.task2.view.events.WindowCloseEvent;

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
        EventManager.registerListener(this);
    }

    public void openGame(RawDataContainer container, TileLinesGame game) {
        try {
            setSession(GameSession.create(this, container, game));
        } catch (Exception e) {
            logger.fatal("Game scene create error", e);
        }
    }

    public void openMenu() {
        try {
            setSession(MenuSession.create(this));
        } catch (Exception e) {
            logger.fatal("Choice scene create error", e);
        }
    }

    public void openGameChoice() {
        try {
            setSession(ChoiceSession.create(this));
        } catch (Exception e) {
            logger.fatal("Choice scene create error", e);
        }
    }

    public void openGameScore() {
        try {
            setSession(ScoreSession.create(this));
        } catch (Exception e) {
            logger.error("Score scene create error", e);
            e.printStackTrace();
        }
    }

    private synchronized void setSession(ControllerSession newSession) {
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

    public void end() {
        view.close();
        if (session != null) {
            session.end();
        }
        EventManager.unregisterListener(this);
    }

}
