package il.ac.technion.cs.smarthouse.database;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.parse4j.ParseException;

import il.ac.technion.cs.smarthouse.system.DispatcherCore;
import il.ac.technion.cs.smarthouse.system.InfoType;

/**
 * 
 * @author Inbal Zukerman
 * @date May 26, 2017
 */

public class DatabaseManagerTest {

    @BeforeClass
    public static void init() {
        ServerManager.initialize();
    }

    @Test
    public void additionAndDeletionTest() {
        final String[] path = new String[2];

        path[0] = InfoType.SENSOR.toString();
        path[1] = "temp";

        try {
            DatabaseManager.addInfo(InfoType.TEST, DispatcherCore.getPathAsString(path), "32");
            Assert.assertNotEquals("", DatabaseManager.getLastEntry(path));
            Assert.assertEquals("test.sensor.temp=32", DatabaseManager.getLastEntry(path));

            DatabaseManager.deleteInfo(InfoType.TEST);
            Assert.assertEquals("", DatabaseManager.getLastEntry(path));

            DatabaseManager.addInfo(InfoType.TEST, DispatcherCore.getPathAsString(path), "55");
            Assert.assertEquals("test.sensor.temp=55", DatabaseManager.getLastEntry(path));

            DatabaseManager.deleteInfo(path);
            Assert.assertEquals("", DatabaseManager.getLastEntry(path));

        } catch (final ParseException e) {
            assert null != null;
        }

    }

}
