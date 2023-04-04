package ru.sidey383.control;

import javafx.scene.image.Image;
import ru.sidey383.event.EventHandler;
import ru.sidey383.event.EventManager;
import ru.sidey383.model.ModelInterface;
import ru.sidey383.model.event.ModelStartTileLinesGameEvent;
import ru.sidey383.model.game.TileLinesGame;
import ru.sidey383.model.settings.AppSettings;
import ru.sidey383.view.SceneControllerFactory;
import ru.sidey383.view.ViewInterface;
import ru.sidey383.view.events.PlayerChangeSceneEvent;
import ru.sidey383.view.events.WindowCloseEvent;
import ru.sidey383.view.game.GameView;
import ru.sidey383.view.menu.MenuView;

import java.io.IOException;

public class Controller {

    private final ViewInterface view;

    private final AppSettings settings;

    private final ModelInterface model;

    private ControllerSession session = null;

    public Controller(ViewInterface view, ModelInterface model) {
        this.view = view;
        this.settings = model.getSettings();
        this.model = model;
        EventManager.manager.registerListener(this);
    }

    public void startGame(TileLinesGame game) {
        SceneControllerFactory<? extends GameView> factory = view.getFactory(GameView.class);
        GameView gameView = null;
        try {
            gameView = factory.createScene();
        } catch (IOException e) {
            //TODO: some logging
        }
        if (gameView == null)
            return;
        gameView.setCenterImage(new Image(game.getCenterImage()));
        gameView.setLeftImage(new Image(game.getLeftImage()));
        gameView.setRightImage(new Image(game.getRightImage()));
        view.setScene(gameView);
        if (session != null)
            session.end();
        session = new GameSession(this, game, gameView);
        session.start();
    }

    public void openMenu() {
        MenuView menuView = null;
        try {
            menuView = view.getFactory(MenuView.class).createScene();
        } catch (IOException e) {
            //TODO: some logging
        }
        if (menuView == null)
            return;
        view.setScene(menuView);
        if (session != null)
            session.end();
        session = new MenuSession(this, menuView);
        session.start();
    }

    @EventHandler
    public void onGameStart(ModelStartTileLinesGameEvent event) {
        startGame(event.getGame());
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
            default -> {}
        }
    }
    public ControllerSession getSession() {
        return session;
    }

    public AppSettings getSettings() {
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
