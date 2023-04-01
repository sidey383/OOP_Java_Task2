package ru.sidey383.event;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class EventManager {

    public static final EventManager manager = new EventManager();

    Map<Class<? extends Event>, List<EventExecutor>> executorMap = new HashMap<>();

    private EventManager() {
    }


    public void registerListener(Object listener) {
        if (listener == null)
            return;
        Class<?> clazz = listener.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isStatic(method.getModifiers()))
                continue;
            if (!method.canAccess(listener))
                continue;
            EventHandler eventHandler = method.getAnnotation(EventHandler.class);
            if (eventHandler == null)
                continue;
            Class<? extends Event> eventClazz = eventHandler.event();
            if (!executorMap.containsKey(eventClazz))
                executorMap.put(eventClazz, new ArrayList<>());
            executorMap.get(eventClazz).add(new EventExecutor(listener, method));
        }
    }

    public void unregisterListener(Object listener) {
        executorMap.values().forEach(
                list -> list.removeIf(
                        executor -> executor.getObject().equals(listener)
                )
        );
    }

    public void runEvent(Event event) {
        Class<? extends Event> clazz = event.getClass();
        List<EventExecutor> executors = executorMap.get(clazz);
        if (event.isAsynchronous()) {
            executors.forEach(e ->
                    new Thread(() -> {
                        try {
                            e.execute(event);
                        } catch (Throwable ex) {
                            ex.printStackTrace();
                        }
                    }).start());
        } else {
            executors.forEach(e -> {
                try {
                    e.execute(event);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            });
        }
    }


}
