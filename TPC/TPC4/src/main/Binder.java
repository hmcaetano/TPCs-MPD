package main;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by HelioCaetano on 12-03-2014.
 */

public class Binder {

    public static Map<String, Object> getFieldsValues(Object o){

        if(o==null)
            throw new IllegalArgumentException();

        Map<String, Object> res = new HashMap<String, Object>();
        Field[] fields = o.getClass().getDeclaredFields();

        for (Field f : fields){
            f.setAccessible(true);
            try{
                res.put(f.getName(), f.get(o));
            }
            catch (IllegalAccessException e){
                //Excepeção do tipo runTime ou derivado n é "chateado" pelo compilador, ao contrário de exception e derivados
                throw new RuntimeException(e);
            }
        }
        return res;
    }


    public static <T> boolean bindMember(T target, Member member, Map<String, Object> memberVals) throws IllegalAccessException, InvocationTargetException {

        if(member.getClass().isAssignableFrom(Field.class))
            return bindField(target, (Field)member, memberVals);
        if(member.getClass().isAssignableFrom(Method.class))
            return bindProperty(target, (Method) member, memberVals);
        return false;
    }



    public static <T> boolean bindField(T target, Field field, Map<String, Object> fieldsVals) throws IllegalAccessException {

        Object obj = fieldsVals.get(field.getName());
        if(obj == null)
            return false;

        Class<?> fieldType = field.getType();
        field.setAccessible(true);
        if(fieldType.isPrimitive())
            fieldType = field.get(target).getClass();

        if(fieldType.isAssignableFrom(obj.getClass())){
            field.set(target, obj);
            return true;
        }
        return false;
    }


    public static <T> T bindToFields(Class<T> klass, Map<String, Object> fieldsVals) throws IllegalAccessException, InstantiationException {
        //Afectar em target os campos que tenham nome igual a um de fields

        T target = klass.newInstance();
        Field[] fields = klass.getDeclaredFields();

        for(Field field : fields)
            bindField(target, field, fieldsVals);

        return target;
    }

    public static <T> boolean bindProperty(T target, Method method, Map<String, Object> vals) throws InvocationTargetException, IllegalAccessException {

        String methodName = method.getName(), propertyName;
        Object myObj;
        Class<?> []parametersTypes;
        Class<?> parameterType;

        if(methodName.substring(0, 3).compareTo("set") != 0)
            return false;
        propertyName = methodName.substring(3);
        if(!vals.containsKey(propertyName))
            return false;
        myObj = vals.get(propertyName);
        parametersTypes = method.getParameterTypes();
        if( parametersTypes.length != 1 )
            return false;
        parameterType = WrapperUtilites.toWrapper( parametersTypes[0] );
        if(parameterType.isAssignableFrom(myObj.getClass())){
            method.setAccessible(true);
            method.invoke(target, myObj);
            return  true;
        }
        return false;
    }
    public static <T> T bindToProperties(Class<T> klass, Map<String, Object> vals) throws IllegalAccessException, InstantiationException, InvocationTargetException {

        Method[] methods = klass.getMethods();
        T target = klass.newInstance();
        Map<String, Object> myMap = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
        myMap.putAll(vals);
        vals = myMap;

        for(Method method : methods)
            bindProperty(target, method, vals);

        return  target;
    }

    public static <T> T bindToFieldsAndProps(Class<T> klass, Map<String, Object> vals) throws IllegalAccessException, InstantiationException, InvocationTargetException {

        T target = klass.newInstance();
        Member[] m = klass.getDeclaredFields();

        ArrayList<Member> members = new ArrayList<Member>(Arrays.asList(m));
        members.addAll(Arrays.asList(klass.getDeclaredMethods()));
        Map<String, Object> myMap = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
        myMap.putAll(vals);
        vals = myMap;


        for(Member member : members)
            bindMember(target, member, vals);

        return target;
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