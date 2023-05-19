package ru.sidey383.task2.model.data.game.read;

import java.util.*;

public class RawDataContainer {

    private final Map<String, Object> data;

    private final String hash;

    private RawDataContainer(Map<String, Object> data, String hash) {
        this.data = data;
        this.hash = hash;
    }

    @SuppressWarnings("unchecked")
    public synchronized  <T> Optional<T> getData(Class<T> type, String name) {
        Object obj = data.get(name);
        if (obj == null || !type.isAssignableFrom(obj.getClass()))
            return Optional.empty();
        return Optional.of((T)obj);
    }

    public String getHash() {
        return hash;
    }

    public static RawDataBuilder builder() {
        return new RawDataBuilder();
    }

    @SuppressWarnings("UnusedReturnValue")
    public static class RawDataBuilder {

        private RawDataBuilder() {}

        private final HashMap<String, Object> data = new HashMap<>();

        private String hash = null;

        public RawDataBuilder withObject(String name, Object object) {
            if (object != null)
                data.put(name, object);
            return this;
        }

        public RawDataBuilder withHash(String hash) {
            this.hash = hash;
            return this;
        }

        public RawDataContainer build() {
            if (hash == null)
                throw new IllegalStateException("Hash not initialized");
            return new RawDataContainer(data, hash);
        }
    }

}
