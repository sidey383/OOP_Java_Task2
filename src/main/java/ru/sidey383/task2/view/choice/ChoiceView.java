package ru.sidey383.task2.view.choice;

import ru.sidey383.task2.view.AppScene;

import java.util.Collection;

public abstract class ChoiceView extends AppScene {

    public abstract void setGameChoice(Collection<GameChoiceUnit> choices);

}
