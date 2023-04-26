package ru.sidey383.control.session;

import ru.sidey383.control.Controller;
import ru.sidey383.control.ControllerSession;
import ru.sidey383.event.EventHandler;
import ru.sidey383.model.game.GameDescription;
import ru.sidey383.view.events.menu.PlayerGameExitEvent;
import ru.sidey383.view.events.menu.PlayerMenuGameStartEvent;
import ru.sidey383.view.menu.MenuView;

import java.net.URL;

public class MenuSession extends ControllerSession {

    private final MenuView menuView;


    public MenuSession(Controller controller, MenuView menuView) {
        super(controller);
        this.menuView = menuView;
    }

    @EventHandler
    public void onGameStart(PlayerMenuGameStartEvent e) {
        try {
            getController().getModel().startGame(new GameDescription() {
                @Override
                public String getName() {
                    return "test";
                }

                @Override
                public URL getGameContainer() {
                    return getClass().getResource("/exampleGame.zip");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onExit(PlayerGameExitEvent e) {
        getController().stop();
    }


}
