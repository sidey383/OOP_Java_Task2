package ru.sidey383.control;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import ru.sidey383.control.session.ChoiceSession;
import ru.sidey383.control.session.GameSession;
import ru.sidey383.control.session.MenuSession;
import ru.sidey383.event.EventHandler;
import ru.sidey383.event.EventManager;
import ru.sidey383.model.ModelInterface;
import ru.sidey383.model.event.ModelStartTileLinesGameEvent;
import ru.sidey383.model.game.TileLinesGame;
import ru.sidey383.model.game.read.DataContainer;
import ru.sidey383.model.settings.AppSettings;
import ru.sidey383.view.ViewInterface;
import ru.sidey383.view.choice.ChoiceView;
import ru.sidey383.view.events.PlayerChangeSceneEvent;
import ru.sidey383.view.events.WindowCloseEvent;
import ru.sidey383.view.game.GameView;
import ru.sidey383.view.menu.MenuView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

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

    public void openGame(DataContainer container, TileLinesGame game) {
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
            Path p =  Files.createTempFile("gameMedia", "");
            try (OutputStream os = Files.newOutputStream(p)) {
                os.write(container
                        .getData(byte[].class, "music")
                        .orElse(new byte[0]));
            } catch (IOException e) {
                //TODO: some log
            }
            gameView.setMusic(new Media(p.toUri().toString()));
            view.setScene(gameView);

        } catch (IOException e) {
            e.printStackTrace();
            //TODO: come logging
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
            //TODO: some logging
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
            //TODO: some logging
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
