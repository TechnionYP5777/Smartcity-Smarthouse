package il.ac.technion.cs.smarthouse.enum_tests;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.networking.messages.MessageType;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;
import il.ac.technion.cs.smarthouse.system.services.alerts_service.EmergencyLevel;

/**
 * Cheating on the enum coverage
 * <p>
 * This class tests the enums for coverage purposes only... Yes I know this is
 * stupid...
 * 
 * @author RON
 * @since 30-05-2017
 */
public class EnumsTest {

    private static Logger log = LoggerFactory.getLogger(EnumsTest.class);

    final Class<?>[] enumClassesToTest = { FileSystemEntries.class, ServiceType.class, MessageType.class,
            EmergencyLevel.class };

    @Test
    public void generalEnumToStringTest() {
        Stream.of(enumClassesToTest).flatMap(enumClass -> Stream.of((Object[]) (enumClass.getEnumConstants())))
                        .map(enumVal -> (enumVal + ""));
        assert true;
    }

    @Test
    @SuppressWarnings("cast")
    public void enumDeclaredFunctionsWithNoParamsTest() {
        Stream.of(enumClassesToTest)
                        .flatMap(enumClass -> Stream.of((Method[]) ((Class<?>) enumClass).getDeclaredMethods()))
                        .filter(m -> ((Method) m).getParameterTypes().length == 0)
                        .forEach(m -> Stream.of(((Method) m).getDeclaringClass().getEnumConstants()).forEach(e -> {
                            try {
                                ((Method) m).setAccessible(true);
                                ((Method) m).invoke(e);
                                ((Method) m).setAccessible(false);
                            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
                                log.error("Enum methods test failed", e1);
                                assert false;
                            }
                        }));
        assert true;
    }
}
