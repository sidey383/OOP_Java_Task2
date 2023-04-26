package ru.sidey383.control.session;

import ru.sidey383.control.Controller;
import ru.sidey383.control.ControllerSession;
import ru.sidey383.event.EventHandler;
import ru.sidey383.view.events.menu.PlayerGameExitEvent;
import ru.sidey383.view.menu.MenuView;

public class MenuSession extends ControllerSession {

    private final MenuView menuView;


    public MenuSession(Controller controller, MenuView menuView) {
        super(controller);
        this.menuView = menuView;
    }

    @EventHandler
    public void onExit(PlayerGameExitEvent e) {
        getController().stop();
    }


}
