package ru.sidey383.task2.control.session.creator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sidey383.task2.control.ControllerSessionCreator;
import ru.sidey383.task2.control.session.GameChoiceSession;
import ru.sidey383.task2.event.EventHandler;
import ru.sidey383.task2.view.choice.ChoiceView;
import ru.sidey383.task2.view.events.PlayerOpenGameChooseEvent;

public class GameChoiceSessionCreator extends ControllerSessionCreator {

    private static final Logger logger = LogManager.getLogger(GameChoiceSessionCreator.class);

    private ChoiceView choiceView = null;

    @EventHandler
    public void onGameChoiceOpen(PlayerOpenGameChooseEvent e) {
        try {
            if (choiceView == null) {
                choiceView = getController().getScene(ChoiceView.class);
            }
            if (choiceView == null) {
                logger.error("Can't create game choice view");
                return;
            }
            GameChoiceSession session = new GameChoiceSession(choiceView);
            getController().setSession(session);
        } catch (Exception ex) {
            logger.fatal("Choice scene create error", ex);
        }
    }

}
