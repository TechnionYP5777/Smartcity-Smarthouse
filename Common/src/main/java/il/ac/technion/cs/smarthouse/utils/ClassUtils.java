package il.ac.technion.cs.smarthouse.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum ClassUtils {
    ;

    private static final Set<Class<?>> WRAPPER_TYPES = new HashSet<>(Arrays.asList(Boolean.class, Character.class,
                    Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Void.class));

    public static boolean isWrapperType(final Class<?> clazz) {
        return WRAPPER_TYPES.contains(clazz);
    }

    public static boolean isPrimitiveType(final Class<?> clazz) {
        return clazz.isPrimitive();
    }
}
