package ru.sidey383.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EventExecutor {

    private Object object;

    private Method method;

    public EventExecutor(Object object, Method method) {
        this.object = object;
        this.method = method;
    }

    public void execute(Event e) throws InvocationTargetException, IllegalAccessException {
        method.invoke(object, e);
    }

    public Object getObject() {
        return object;
    }


}
