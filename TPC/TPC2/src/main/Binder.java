package main;

import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by HelioCaetano on 27-03-2014.
 */
public class Binder {

    public static Map<String, Object> getFieldsValues(Object o) throws IllegalAccessException {
        if(o == null)
            throw new IllegalArgumentException();

        Map<String, Object> myMap = new HashMap<String, Object>();
        Field[] fields = o.getClass().getDeclaredFields();

        for(Field field : fields){
            field.setAccessible(true);
            myMap.put(field.getName(), field.get(o));
        }
        return  myMap;
    }

    public static <T> T bindTo(Class<T> klass, Map<String, Object> fieldsVals) throws IllegalAccessException, InstantiationException {
        if(klass == null | fieldsVals == null)
            throw new IllegalArgumentException();

        T target = klass.newInstance();
        Field[] fields = klass.getDeclaredFields();
        Class<?> fieldType;
        Object obj;

        for (Field field : fields){
            obj = fieldsVals.get(field.getName());

            if(obj == null)
                continue;

            fieldType = field.getType();
            field.setAccessible(true);

            if(fieldType.isPrimitive())
                fieldType = field.get(target).getClass();

            if(fieldType.isAssignableFrom(obj.getClass()))
                field.set(target, obj);
        }
        return target;
    }

}
