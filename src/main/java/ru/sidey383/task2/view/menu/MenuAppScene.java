package ru.sidey383.task2.view.menu;

import ru.sidey383.task2.event.EventManager;
import ru.sidey383.task2.view.events.PlayerChangeSceneEvent;
import ru.sidey383.task2.view.events.menu.PlayerGameExitEvent;

public class MenuAppScene extends MenuView {

    public void startAction() {
        EventManager.runEvent(new PlayerChangeSceneEvent(PlayerChangeSceneEvent.AvailableScene.GAME_CHOOSE));
    }

    public void scoreAction() {
        EventManager.runEvent(new PlayerChangeSceneEvent(PlayerChangeSceneEvent.AvailableScene.SCORE));
    }

    public void exitAction() {
        EventManager.runEvent(new PlayerGameExitEvent());
    }
}
