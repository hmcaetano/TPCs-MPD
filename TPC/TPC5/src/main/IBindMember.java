package main;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by HelioCaetano on 27-03-2014.
 */
public interface IBindMember {
    boolean bind(Object target, String key, String v) throws  IllegalArgumentException, InvocationTargetException, IllegalAccessException ;
}
