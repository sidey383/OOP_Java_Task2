package ru.sidey383.task2.control.session.creator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sidey383.task2.control.ControllerSessionCreator;
import ru.sidey383.task2.control.session.ScoreSession;
import ru.sidey383.task2.event.EventHandler;
import ru.sidey383.task2.view.events.PlayerOpenScoreEvent;
import ru.sidey383.task2.view.score.ScoreView;

public class ScoreSessionCreator extends ControllerSessionCreator {

    private static final Logger logger = LogManager.getLogger(ScoreSessionCreator.class);

    private ScoreView choiceView;

    @EventHandler
    public void onScoreOpen(PlayerOpenScoreEvent e) {
        try {
            if (choiceView == null) {
                choiceView = controller().getScene(ScoreView.class);
            }
            if (choiceView == null) {
                logger.error("Can't create choice view");
                return;
            }
            ScoreSession scoreSession = new ScoreSession(choiceView);
            controller().setSession(scoreSession);
            scoreSession.updateScores();
        } catch (Exception ex) {
            logger.error("Score scene create error", ex);
        }
    }

}
