package il.ac.technion.cs.eldery.system;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
}
