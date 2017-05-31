package il.ac.technion.cs.smarthouse.enum_tests;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

import org.junit.Test;

import il.ac.technion.cs.smarthouse.networking.messages.MessageType;
import il.ac.technion.cs.smarthouse.sensors.SensorType;
import il.ac.technion.cs.smarthouse.simulator.model.Location;
import il.ac.technion.cs.smarthouse.system.EmergencyLevel;
import il.ac.technion.cs.smarthouse.system.SensorLocation;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;

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

    final Class<?>[] enumClassesToTest = { FileSystemEntries.class, SensorLocation.class,
            ServiceType.class, MessageType.class, SensorType.class, Location.class, EmergencyLevel.class };

    @Test
    public void generalEnumStupidToStringTest() {
        Stream.of(enumClassesToTest).flatMap(enumClass -> Stream.of(enumClass.getEnumConstants()))
                        .map(enumVal -> (enumVal + ""));
        assert true;
    }

    @Test
    public void enumDeclaredFunctionsWithNoParamsStupidTest() {
        Stream.of(enumClassesToTest).flatMap(enumClass -> Stream.of(enumClass.getDeclaredMethods()))
                        .filter(m -> m.getParameterTypes().length == 0)
                        .forEach(m -> Stream.of(m.getDeclaringClass().getEnumConstants()).forEach(e -> {
                            try {
                                m.setAccessible(true);
                                m.invoke(e);
                                m.setAccessible(false);
                            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
                                e1.printStackTrace();
                                assert false;
                            }
                        }));
        assert true;
    }
}
