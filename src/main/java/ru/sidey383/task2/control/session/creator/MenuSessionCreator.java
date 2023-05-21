package ru.sidey383.task2.control.session.creator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sidey383.task2.control.ControllerSessionCreator;
import ru.sidey383.task2.control.session.MenuSession;
import ru.sidey383.task2.event.EventHandler;
import ru.sidey383.task2.view.events.PlayerOpenMenuEvent;
import ru.sidey383.task2.view.menu.MenuView;

public class MenuSessionCreator extends ControllerSessionCreator {

    private static final Logger logger = LogManager.getLogger(MenuSessionCreator.class);

    private MenuView menuView = null;

    @EventHandler
    public void onMenuOpen(PlayerOpenMenuEvent e) {
        try {
            if (menuView == null) {
                menuView = controller().getScene(MenuView.class);
            }
            if (menuView == null) {
                logger.error("Can't create menu view");
                return;
            }
            controller().setSession(new MenuSession(menuView));
        } catch (Exception ex) {
            logger.fatal("Choice scene create error", ex);
        }
    }

}
