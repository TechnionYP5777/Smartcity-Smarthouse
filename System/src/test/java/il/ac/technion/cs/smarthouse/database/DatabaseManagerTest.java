package il.ac.technion.cs.smarthouse.database;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.parse4j.ParseException;

import il.ac.technion.cs.smarthouse.system.Dispatcher;
import il.ac.technion.cs.smarthouse.system.DispatcherCore;
import il.ac.technion.cs.smarthouse.system.InfoType;

/**
 * 
 * @author Inbal Zukerman
 * @date May 26, 2017
 */

public class DatabaseManagerTest {

    private final DatabaseManager dbManager = new DatabaseManager();

    /*
    @Test
    public void additionAndDeletionTest() {
        final String[] path = new String[2];

        path[0] = InfoType.SENSOR.toString();
        path[1] = "temp";

        try {
            dbManager.addInfo(InfoType.TEST, DispatcherCore.getPathAsString(path), "32");
            Assert.assertNotEquals("", dbManager.getLastEntry(path));
            Assert.assertEquals("test.sensor.temp=32", dbManager.getLastEntry(path));

            dbManager.deleteInfo(InfoType.TEST);
            Assert.assertEquals("", dbManager.getLastEntry(path));

            dbManager.addInfo(InfoType.TEST, DispatcherCore.getPathAsString(path), "55");
            Assert.assertEquals("test.sensor.temp=55", dbManager.getLastEntry(path));

            dbManager.deleteInfo(path);
            Assert.assertEquals("", dbManager.getLastEntry(path));

        } catch (final ParseException e) {
            assert null != null;
        }

    }*/

    @Ignore @Test
    public void testLastEntry() {
        final String[] path = new String[3];

        path[0] = InfoType.SENSOR.toString();
        path[1] = "temp";
        path[2] = "house";
        try {
            dbManager.addInfo(InfoType.TEST, DispatcherCore.getPathAsString(path), "10");

            path[2] = "garden";
            dbManager.addInfo(InfoType.TEST, DispatcherCore.getPathAsString(path), "23");

            String lastEntry = dbManager.getLastEntry(InfoType.SENSOR.toString() + Dispatcher.DELIMITER + "temp");
            Assert.assertEquals("test.sensor.temp.garden=23", lastEntry);

            path[2] = "house";
            lastEntry = dbManager.getLastEntry(path);
            Assert.assertEquals("test.sensor.temp.house=10", lastEntry);
            
           /* Collection<String> tmp = dbManager.getPathChildren(InfoType.SENSOR.toString());
            
            for (String str : tmp){
                System.out.println(str + "\n");
            }
*/
            dbManager.deleteInfo(InfoType.TEST);
        } catch (ParseException e) {
            Assert.assertNotNull(null);
        }
    }

}
