package ru.sidey383.task2.control.session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sidey383.task2.control.Controller;
import ru.sidey383.task2.control.ControllerSession;
import ru.sidey383.task2.model.data.game.GameDescription;
import ru.sidey383.task2.view.choice.ChoiceView;
import ru.sidey383.task2.view.choice.GameChoiceUnit;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class ChoiceSession extends ControllerSession {

    private static final Logger logger = LogManager.getLogger(ChoiceSession.class);

    private ChoiceSession(Controller controller) {
        super(controller);
    }

    public static ChoiceSession create(Controller controller) throws IOException {
        ChoiceView choiceView = controller.getView().getScene(ChoiceView.class);
        choiceView.setGameChoice(
                controller.getModel().getGameDescriptions().stream()
                        .filter(Objects::nonNull)
                        .map(d -> new GameChoiceUnit() {
                            @Override
                            public String getText() {
                                return d.getName();
                            }

                            @Override
                            public void apply() {
                                try {
                                    controller.getModel().startGame(d);
                                } catch (Exception e) {
                                    logger.fatal("Game start error", e);
                                }
                            }
                        })
                        .collect(Collectors.toList())
        );
        controller.getView().setScene(choiceView);
        return new ChoiceSession(controller);
    }

}
