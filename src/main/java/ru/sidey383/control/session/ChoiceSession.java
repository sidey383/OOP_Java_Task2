package ru.sidey383.control.session;

import ru.sidey383.control.Controller;
import ru.sidey383.control.ControllerSession;
import ru.sidey383.model.game.GameDescription;
import ru.sidey383.view.choice.ChoiceView;
import ru.sidey383.view.choice.GameChoiceUnit;

import java.util.Collection;

public class ChoiceSession extends ControllerSession {

    private final ChoiceView view;

    public ChoiceSession(Controller controller, ChoiceView view, Collection<GameDescription> descriptions) {
        super(controller);
        view.setGameChoice(descriptions.stream().map(d -> new GameChoiceUnit() {
            @Override
            public String getText() {
                return d.getName();
            }

            @Override
            public void apply() {

            }
        }).toList());
        this.view = view;
    }

}
