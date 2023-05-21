package ru.sidey383.task2.control.session;

import ru.sidey383.task2.control.Controller;
import ru.sidey383.task2.control.ControllerSession;
import ru.sidey383.task2.control.exception.ControllerException;
import ru.sidey383.task2.control.session.creator.GameSessionCreator;
import ru.sidey383.task2.event.Event;
import ru.sidey383.task2.event.GameChoiceEvent;
import ru.sidey383.task2.model.data.game.GameDescription;
import ru.sidey383.task2.view.AppScene;
import ru.sidey383.task2.view.choice.ChoiceView;
import ru.sidey383.task2.view.choice.GameChoiceUnit;

import java.util.Objects;
import java.util.stream.Collectors;

public class GameChoiceSession extends ControllerSession {

    private final ChoiceView choiceView;

    private GameSessionCreator gameSessionCreator;

    public GameChoiceSession(ChoiceView choiceView) {
        this.choiceView = choiceView;

    }

    @Override
    public void start(Controller controller) throws ControllerException {
        super.start(controller);
        updateDescriptions();
        this.gameSessionCreator = new GameSessionCreator(this);
        getController().addSessionCreator(gameSessionCreator);
    }

    @Override
    public void stop() throws ControllerException {
        super.stop();
        getController().removeSessionCreator(gameSessionCreator);
    }

    public void updateDescriptions() {
        choiceView.setGameChoice(
                getController().getModel().getGameDescriptions()
                        .stream()
                        .filter(Objects::nonNull)
                        .map(ViewChoiceUint::new)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public AppScene getScene() {
        return choiceView;
    }

    private record ViewChoiceUint(GameDescription description) implements GameChoiceUnit {

        @Override
            public String getText() {
                return description.getName();
            }

            @Override
            public Event gameStartEvent() {
                return new GameChoiceEvent(description);
            }

    }

}
