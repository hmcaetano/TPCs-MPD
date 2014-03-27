package tests;

import main.Binder;
import main.Pessoa;
import java.util.Map;

/**
 * Created by HelioCaetano on 27-03-2014.
 */
public class BinderTests {

    public static void main(String[] args) throws IllegalAccessException {

        Pessoa p = new Pessoa(1, 20, "João");
        Map<String, Object> map = Binder.getFieldsValues(p);

        for(Map.Entry e : map.entrySet())
            System.out.println(e.getKey() + " = " + e.getValue());
    }
}
