package ru.sidey383.control;

import javafx.scene.image.Image;
import ru.sidey383.event.EventHandler;
import ru.sidey383.event.EventManager;
import ru.sidey383.model.event.ModelStartTileLinesGameEvent;
import ru.sidey383.model.game.ClickType;
import ru.sidey383.model.game.TileLinesGame;
import ru.sidey383.model.settings.AppSettings;
import ru.sidey383.view.SceneControllerFactory;
import ru.sidey383.view.ViewInterface;
import ru.sidey383.view.events.GameKeyEvent;
import ru.sidey383.view.events.WindowCloseEvent;
import ru.sidey383.view.game.GameView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Controller {

    private final ViewInterface view;

    private final AppSettings settings;

    public Controller(ViewInterface view, AppSettings settings) {
        this.view = view;
        this.settings = settings;
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
        Map<Integer, ClickType> keyMap = new HashMap<>();
        for (Map.Entry<ClickType, Integer> e : settings.gameKeys().entrySet()) {
            keyMap.put(e.getValue(), e.getKey());
        }
        new GameController(game, gameView, keyMap);
    }

    @EventHandler
    public void onGameStart(ModelStartTileLinesGameEvent event) {
        startGame(event.getGame());
    }

    @EventHandler
    public void onWindowsClose(WindowCloseEvent e) {}

}
