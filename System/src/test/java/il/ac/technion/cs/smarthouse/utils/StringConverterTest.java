package il.ac.technion.cs.smarthouse.utils;

import java.lang.reflect.Field;

import org.junit.Test;
import com.google.gson.Gson;

public class StringConverterTest {
    
    @Test
    public void convertWithNoExceptionsTest() throws IllegalArgumentException, IllegalAccessException {
        TestObj e = new TestObj();

        for (Field field : e.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            
            if (!"abcdefg".contains(field.getName()))
                continue;

            System.out.print(field.getType() + " "+field.getName()+"\t\t" + field.get(e)+" | "+new Gson().toJson(field.get(e))+" | "+
            new Gson().fromJson(new Gson().toJson(field.get(e)), field.getType()));

            field.set(e, StringConverter.convert(field.getType(), field.get(e).toString()));
            System.out.print(" | " + field.get(e));
            field.set(e, StringConverter.convert(field.getType(), null));
            System.out.println(" | " + field.get(e) + " |");
        }
    }
}

class TestObj {
    int a = 9;
    Integer b = 58;
    boolean c = true;
    float d = 55;
    double e = 49.9;
    char f = 'a';
    String g = "BYE";
}

