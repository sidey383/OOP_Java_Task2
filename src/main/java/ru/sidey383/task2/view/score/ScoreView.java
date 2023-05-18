package ru.sidey383.task2.view.score;

import ru.sidey383.task2.view.AppScene;

import java.util.Collection;

public abstract class ScoreView extends AppScene {

    public abstract void setGameChoice(Collection<ScoreUnit> choices);

}
