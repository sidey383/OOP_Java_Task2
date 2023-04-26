package ru.sidey383.model.game.read;

import java.util.*;

public class DataContainer {

    private final HashMap<String, Object> data = new HashMap<>();

    public DataContainer() {}

    public synchronized void addObject(String name, Object object) {
        if (object != null)
                data.put(name, object);
    }

    @SuppressWarnings("unchecked")
    public synchronized  <T> Optional<T> getData(Class<T> type, String name) {
        Object obj = data.get(name);
        if (obj == null || !type.isAssignableFrom(obj.getClass()))
            return Optional.empty();
        return Optional.of((T)obj);
    }

}
