package ru.sidey383.view.events;

import javafx.scene.input.KeyCode;
import ru.sidey383.event.Event;

public class GameKeyEvent extends Event {

    private final KeyCode keyCode;

    private final boolean isPress;

    private final long createTime;

    public GameKeyEvent(boolean isPress, KeyCode keyCode) {
        super(true);
        this.keyCode = keyCode;
        this.isPress = isPress;
        createTime = System.nanoTime();
    }

    public KeyCode getKeyCode() {
        return keyCode;
    }

    public boolean isPress() {
        return isPress;
    }

    public long getCreateNanoTine() {
        return createTime;
    }


}
