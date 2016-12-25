package il.ac.technion.cs.eldery.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import il.ac.technion.cs.eldery.system.exceptions.SensorNotFoundException;
import il.ac.technion.cs.eldery.utils.Table;

public class DatabaseHandlerTest {
    private DatabaseHandler handler;

    @Before public void init() {
        handler = new DatabaseHandler();
    }

    @Test public void emptyCommNameListWhenNoSensors() {
        Assert.assertEquals(new ArrayList<>(), handler.getSensors("iStoves"));
    }

    @Test public void oneSensorIdInOneCommName() {
        handler.addSensor("00:11:22:33:44:55", "iStoves", 100);

        Assert.assertEquals(1, handler.getSensors("iStoves").size());
        Assert.assertEquals(new ArrayList<>(Arrays.asList("00:11:22:33:44:55")), handler.getSensors("iStoves"));
    }

    @Test public void manySensorsIdsInOneCommName() {
        handler.addSensor("00:11:22:33:44:55", "hiney", 100);
        handler.addSensor("11:11:22:33:44:55", "hiney", 100);
        handler.addSensor("22:11:22:33:44:55", "hiney", 100);
        handler.addSensor("33:11:22:33:44:55", "hiney", 100);
        handler.addSensor("44:11:22:33:44:55", "hiney", 100);
        handler.addSensor("55:11:22:33:44:55", "hiney", 100);

        Assert.assertEquals(6, handler.getSensors("hiney").size());
        Assert.assertEquals(new ArrayList<>(Arrays.asList("00:11:22:33:44:55", "11:11:22:33:44:55", "22:11:22:33:44:55", "33:11:22:33:44:55",
                "44:11:22:33:44:55", "55:11:22:33:44:55")), handler.getSensors("hiney"));
    }

    @Test public void multipleCommNames() {
        handler.addSensor("00:11:22:33:44:55", "iStoves", 100);
        handler.addSensor("11:11:22:33:44:55", "iStoves", 100);
        handler.addSensor("22:11:22:33:44:55", "iStoves", 100);

        handler.addSensor("33:11:22:33:44:55", "hiney", 100);
        handler.addSensor("44:11:22:33:44:55", "hiney", 100);
        handler.addSensor("55:11:22:33:44:55", "hiney", 100);

        Assert.assertEquals(3, handler.getSensors("iStoves").size());
        Assert.assertEquals(new ArrayList<>(Arrays.asList("00:11:22:33:44:55", "11:11:22:33:44:55", "22:11:22:33:44:55")),
                handler.getSensors("iStoves"));

        Assert.assertEquals(3, handler.getSensors("hiney").size());
        Assert.assertEquals(new ArrayList<>(Arrays.asList("33:11:22:33:44:55", "44:11:22:33:44:55", "55:11:22:33:44:55")),
                handler.getSensors("hiney"));
    }

    @Test(expected = SensorNotFoundException.class) public void throwsExceptionWhenAddingListenerToNonExistingSensor()
            throws SensorNotFoundException {
        handler.addListener("Some id", null);
    }

    @Test public void addListenerToExistingSensorWithoutException() throws SensorNotFoundException {
        handler.addSensor("00:11:22:33:44:55", "iStoves", 100);
        handler.addListener("00:11:22:33:44:55", null);
    }

    @Test(expected = SensorNotFoundException.class) public void throwsExceptionWhenGettingTableForNonExistingSensor() throws SensorNotFoundException {
        handler.getTable("Some id");
    }

    @Test public void getTableOfExistingSensorWithoutException() throws SensorNotFoundException {
        handler.addSensor("00:11:22:33:44:55", "iStoves", 100);
        handler.getTable("00:11:22:33:44:55");
    }

    @Test public void listenerIsCalledWhenAddingEntryToSensorTable() throws SensorNotFoundException {
        @SuppressWarnings("unchecked") final Consumer<Table<String, String>> listener = Mockito.mock(Consumer.class);

        handler.addSensor("00:22:44:66:88:00", "iStoves", 100);
        handler.addListener("00:22:44:66:88:00", listener);
        handler.getTable("00:22:44:66:88:00").addEntry(null);

        Mockito.verify(listener, Mockito.times(1)).accept(Matchers.any());
    }

    @Test public void listenerIsCalledMultipleTimesWhenAddingMultipleEntries() throws SensorNotFoundException {
        @SuppressWarnings("unchecked") final Consumer<Table<String, String>> listener = Mockito.mock(Consumer.class);

        handler.addSensor("00:22:44:66:88:00", "iStoves", 100);
        handler.addListener("00:22:44:66:88:00", listener);
        handler.getTable("00:22:44:66:88:00").addEntry(null);
        handler.getTable("00:22:44:66:88:00").addEntry(null);
        handler.getTable("00:22:44:66:88:00").addEntry(null);
        handler.getTable("00:22:44:66:88:00").addEntry(null);

        Mockito.verify(listener, Mockito.times(4)).accept(Matchers.any());
    }
}
