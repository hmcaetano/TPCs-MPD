package main;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

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
}
