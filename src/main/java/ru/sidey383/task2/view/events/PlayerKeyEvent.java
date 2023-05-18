package ru.sidey383.task2.view.events;

import javafx.scene.input.KeyCode;
import ru.sidey383.task2.event.Event;

public class PlayerKeyEvent extends Event {

    private final KeyCode keyCode;

    private final boolean isPress;

    private final long createTime;

    public PlayerKeyEvent(boolean isPress, KeyCode keyCode) {
        super(false);
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

    public static enum KeyAction {
        PRESS, RELEASE
    }


}
