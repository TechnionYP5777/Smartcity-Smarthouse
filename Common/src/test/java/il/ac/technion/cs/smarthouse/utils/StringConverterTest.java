package il.ac.technion.cs.smarthouse.utils;

import java.lang.reflect.Field;

import org.junit.Test;

/**
 * @author RON
 * @since 30-05-2017
 */
public class StringConverterTest {

    @Test
    @SuppressWarnings("static-method")
    public void convertWithNoExceptionsTest() throws IllegalArgumentException {
        for (Field field : (new TestObj()).getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (!"abcdefg".contains(field.getName()))
                continue;
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
