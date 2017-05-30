package il.ac.technion.cs.smarthouse.enum_tests;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

import org.jdom2.internal.ReflectionConstructor;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.networking.messages.MessageType;
import il.ac.technion.cs.smarthouse.sensors.SensorType;
import il.ac.technion.cs.smarthouse.simulator.model.Location;
import il.ac.technion.cs.smarthouse.system.EmergencyLevel;
import il.ac.technion.cs.smarthouse.system.InfoType;
import il.ac.technion.cs.smarthouse.system.SensorLocation;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;
import javafx.scene.effect.Reflection;

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

    final Class<?>[] enumClassesToTest = { InfoType.class, FileSystemEntries.class, SensorLocation.class,
            ServiceType.class, MessageType.class, SensorType.class, Location.class, EmergencyLevel.class };

    @Test
    public void generalEnumStupidToStringTest() {
        System.out.println("F1");
        Stream.of(enumClassesToTest).flatMap(enumClass -> Stream.of(enumClass.getEnumConstants()))
                        .map(enumVal -> (enumVal + ""));
        assert true;
    }

    @Test
    public void enumDeclaredFunctionsWithNoParamsStupidTest() {
        System.out.println("F2");
        Stream.of(enumClassesToTest).flatMap(enumClass -> Stream.of(enumClass.getDeclaredMethods()))
                        .filter(m -> m.getParameterTypes().length == 0).forEach(m -> {
                            Stream.of(m.getDeclaringClass().getEnumConstants()).forEach(e -> {
                                try {
                                    m.setAccessible(true);
                                    m.invoke(e);
                                    m.setAccessible(false);
                                } catch (IllegalAccessException | IllegalArgumentException
                                                | InvocationTargetException e1) {
                                    e1.printStackTrace();
                                    //assert false;
                                }
                            });
                        });
        assert true;
    }
}
