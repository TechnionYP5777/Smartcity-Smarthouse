package il.ac.technion.cs.smarthouse.utils;

import org.junit.Test;

/**
 * @author RON
 * @since 19-06-2017
 */
public class ClassUtilsTest {
    @Test
    @SuppressWarnings("static-method")
    public void mainTest() {
        assert !ClassUtils.isPrimitiveType(Integer.class);
        assert ClassUtils.isWrapperType(Integer.class);
        assert !ClassUtils.isPrimitiveType(String.class);
        assert !ClassUtils.isWrapperType(String.class);
        assert ClassUtils.isPrimitiveType(Integer.TYPE);
        assert !ClassUtils.isWrapperType(Integer.TYPE);
    }
}
