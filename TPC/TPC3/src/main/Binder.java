package main;

import java.lang.reflect.*;
import java.util.*;

/**
 * Created by HelioCaetano on 12-03-2014.
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

    public static <T> T bindToProperties(Class<T> klass, Map<String, Object> vals) throws IllegalAccessException, InstantiationException, InvocationTargetException {

        Method[] methods = klass.getMethods();
        T target = klass.newInstance();
        Map<String, Object> myMap = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
        myMap.putAll(vals);
        vals = myMap;

        for(Method method : methods){
            String methodName = method.getName(), propertyName;
            Object myObj;
            Class<?> []parametersTypes;
            Class<?> parameterType;

            if(methodName.substring(0, 3).compareTo("set") != 0)
                continue;
            propertyName = methodName.substring(3);
            if(!vals.containsKey(propertyName))
                continue;
            myObj = vals.get(propertyName);
            parametersTypes = method.getParameterTypes();
            if( parametersTypes.length != 1 )
                continue;
            parameterType = WrapperUtilites.toWrapper( parametersTypes[0] );
            if(parameterType.isAssignableFrom(myObj.getClass())){
                method.setAccessible(true);
                method.invoke(target, myObj);
            }
        }
        return  target;
    }
}
class WrapperUtilites {

    final static Map<Class<?>, Class<?>> wrappers = new HashMap<Class<?>, Class<?>>();

    static {
        wrappers.put(boolean.class, Boolean.class);
        wrappers.put(short.class, Short.class);
        wrappers.put(boolean.class, Boolean.class);
        wrappers.put(int.class, Integer.class);

    }

    public static Class<?> toWrapper(Class<?> c) {
        return c.isPrimitive() ? wrappers.get(c) : c;
    }
}