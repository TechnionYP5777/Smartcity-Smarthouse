package il.ac.technion.cs.smarthouse.database;

import org.junit.Assert;
import org.junit.Test;
import org.parse4j.ParseException;

import il.ac.technion.cs.smarthouse.system.database.cloud_server.DatabaseManager;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;

/**
 * 
 * @author Inbal Zukerman
 * @date May 26, 2017
 */

public class DatabaseManagerTest {

    private final DatabaseManager dbManager = new DatabaseManager();
    
   
    @Test
    public void additionAndDeletionTest() {


        try {
            dbManager.addInfo(FileSystemEntries.TESTS_SENSORS_DATA.buildPath(), "32");
            Assert.assertNotEquals("", dbManager.getLastEntry(FileSystemEntries.TESTS_SENSORS_DATA.buildPath()));
            
            
            Assert.assertEquals("32", dbManager.getLastEntry(FileSystemEntries.TESTS_SENSORS_DATA.buildPath()).data);

            dbManager.deleteInfo(FileSystemEntries.TESTS.buildPath());
            Assert.assertNull(dbManager.getLastEntry(FileSystemEntries.TESTS.buildPath()).data);
            

            dbManager.addInfo(FileSystemEntries.TESTS_SENSORS_DATA.buildPath(), "55");
            Assert.assertEquals("55", dbManager.getLastEntry(FileSystemEntries.TESTS_SENSORS_DATA.buildPath()).data);

            dbManager.deleteInfo(FileSystemEntries.TESTS_SENSORS_DATA);
            Assert.assertNull(dbManager.getLastEntry(FileSystemEntries.TESTS_SENSORS_DATA.buildPath()).data);

        } catch (final ParseException e) {
            assert null != null;
        }

    }


}
