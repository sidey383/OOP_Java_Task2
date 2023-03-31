package ru.sidey383.event;

public abstract class Event {

    private boolean isAsync;

    public Event(boolean isAsync) {
        this.isAsync = isAsync;
    }

    public boolean isAsynchronous() {
        return isAsync;
    }

}
