package ru.sidey383.task2.control.session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sidey383.task2.control.Controller;
import ru.sidey383.task2.control.ControllerSession;
import ru.sidey383.task2.model.data.game.GameDescription;
import ru.sidey383.task2.model.exception.ModelException;
import ru.sidey383.task2.view.AppScene;
import ru.sidey383.task2.view.choice.ChoiceView;
import ru.sidey383.task2.view.choice.GameChoiceUnit;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class ChoiceSession extends ControllerSession {

    private static final Logger logger = LogManager.getLogger(ChoiceSession.class);

    ChoiceView choiceView;

    private ChoiceSession(Controller controller, ChoiceView choiceView) {
        super(controller);
        this.choiceView = choiceView;

    }

    public static ChoiceSession create(Controller controller) throws IOException {
        ChoiceView choiceView = controller.getView().getScene(ChoiceView.class);
        ChoiceSession session = new ChoiceSession(controller, choiceView);
        session.setDescriptions(controller.getModel().getGameDescriptions());
        return session;
    }

    private void setDescriptions(Collection<GameDescription> descriptions) {
        choiceView.setGameChoice(
                descriptions.stream()
                        .filter(Objects::nonNull)
                        .map(ViewChoiceUint::new)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public AppScene getScene() {
        return choiceView;
    }

    private class ViewChoiceUint implements GameChoiceUnit {

        private final GameDescription description;

        public ViewChoiceUint(GameDescription description) {
            this.description = description;
        }

        @Override
        public String getText() {
            return description.getName();
        }

        @Override
        public void apply() {
            try {
                getController().getModel().startGame(description);
            } catch (ModelException e) {
                logger.fatal("Game start error", e);
            }
        }
    }

}
