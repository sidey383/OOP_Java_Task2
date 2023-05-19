package ru.sidey383.task2.control.session;

import ru.sidey383.task2.control.Controller;
import ru.sidey383.task2.control.ControllerSession;
import ru.sidey383.task2.event.EventHandler;
import ru.sidey383.task2.view.AppScene;
import ru.sidey383.task2.view.events.menu.PlayerGameExitEvent;
import ru.sidey383.task2.view.menu.MenuView;

import java.io.IOException;

public class MenuSession extends ControllerSession {

    private final MenuView menuView;

    private MenuSession(Controller controller, MenuView menuView) {
        super(controller);
        this.menuView = menuView;
    }

    @Override
    public AppScene getScene() {
        return menuView;
    }

    public static MenuSession create(Controller controller) throws IOException {
        MenuView menuView = controller.getView().getScene(MenuView.class);
        return new MenuSession(controller, menuView);
    }

    @EventHandler
    public void onExit(PlayerGameExitEvent e) {
        getController().end();
    }


}
