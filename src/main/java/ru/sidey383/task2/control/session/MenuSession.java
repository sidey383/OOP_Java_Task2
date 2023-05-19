package ru.sidey383.task2.control.session;

import ru.sidey383.task2.control.Controller;
import ru.sidey383.task2.control.ControllerSession;
import ru.sidey383.task2.event.EventHandler;
import ru.sidey383.task2.view.events.menu.PlayerGameExitEvent;
import ru.sidey383.task2.view.menu.MenuView;

import java.io.IOException;

public class MenuSession extends ControllerSession {

    private MenuSession(Controller controller) {
        super(controller);
    }

    public static MenuSession create(Controller controller) throws IOException {
        MenuView menuView = controller.getView().getScene(MenuView.class);
        controller.getView().setScene(menuView);
        return new MenuSession(controller);
    }

    @EventHandler
    public void onExit(PlayerGameExitEvent e) {
        getController().end();
    }


}
