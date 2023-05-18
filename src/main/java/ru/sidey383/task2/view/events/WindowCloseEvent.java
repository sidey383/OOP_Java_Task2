package ru.sidey383.task2.view.events;

import ru.sidey383.task2.event.Event;

public class WindowCloseEvent extends Event {
    public WindowCloseEvent() {
        super(false);
    }
}
