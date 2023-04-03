package ru.sidey383.model.event;

import ru.sidey383.event.Event;
import ru.sidey383.model.settings.AppSettings;

public class SettingsUpdateEvent extends Event {

    private final AppSettings settings;
    public SettingsUpdateEvent(AppSettings settings) {
        super(true);
        this.settings = settings;
    }

    AppSettings getSettings() {
        return settings;
    }

}
