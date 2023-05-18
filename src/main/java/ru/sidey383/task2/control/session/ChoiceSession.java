package ru.sidey383.task2.control.session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sidey383.task2.control.Controller;
import ru.sidey383.task2.control.ControllerSession;
import ru.sidey383.task2.model.data.game.GameDescription;
import ru.sidey383.task2.view.choice.ChoiceView;
import ru.sidey383.task2.view.choice.GameChoiceUnit;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class ChoiceSession extends ControllerSession {

    private final Logger logger = LogManager.getLogger(ChoiceSession.class);

    private final ChoiceView view;

    public ChoiceSession(Controller controller, ChoiceView view, Collection<GameDescription> descriptions) {
        super(controller);
        view.setGameChoice(descriptions.stream().filter(Objects::nonNull).map(d -> new GameChoiceUnit() {
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
        }).collect(Collectors.toList()));

        this.view = view;
    }

}
