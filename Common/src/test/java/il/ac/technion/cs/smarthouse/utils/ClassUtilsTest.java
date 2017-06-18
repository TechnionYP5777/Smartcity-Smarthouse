package il.ac.technion.cs.smarthouse.utils;

import org.junit.Test;

public class ClassUtilsTest {
    @Test
    public void mainTest() {
        assert !ClassUtils.isPrimitiveType(Integer.class);
        assert ClassUtils.isWrapperType(Integer.class);
        
        assert !ClassUtils.isPrimitiveType(String.class);
        assert !ClassUtils.isWrapperType(String.class);
        
        assert ClassUtils.isPrimitiveType(Integer.TYPE);
        assert !ClassUtils.isWrapperType(Integer.TYPE);
    }
}
