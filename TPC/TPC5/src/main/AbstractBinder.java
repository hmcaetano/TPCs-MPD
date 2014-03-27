package main;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by HelioCaetano on 27-03-2014.
 */
public abstract class AbstractBinder {

    public <T> T bindTo(Class<T> klass, Map<String, Object> vals)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (klass == null || vals == null) {
            throw new IllegalArgumentException();
        }
        T target = klass.newInstance();
        for (Map.Entry<String, Object> e : vals.entrySet()) {
            bindMember(target, e.getKey(), e.getValue());
        }
        return target;
    }

    abstract <T> boolean bindMember(T target, String key, Object value)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;
}
