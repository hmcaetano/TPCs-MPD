package main;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by HelioCaetano on 27-03-2014.
 */
public class BinderProps implements IBindMember{

    @Override
    public boolean bind(Object target, String key, String v) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvocationTargetException {
        Method[] ms = target.getClass().getMethods();
        for (Method m : ms) {
            String mName = m.getName();
            if (!mName.equalsIgnoreCase("set" + key)) {
                continue;
            }
            Class<?>[] paramsKlasses = m.getParameterTypes();
            if (paramsKlasses.length != 1) {
                continue;
            }
            Class<?> propType = WrapperUtilites.toWrapper(paramsKlasses[0]);
            if (propType.isAssignableFrom(v.getClass())) {
                m.setAccessible(true);
                m.invoke(target, v);
                return true;
            }
        }
        return false;

    }
}
