package il.ac.technion.cs.smarthouse.system.file_system;

import org.junit.Assert;
import org.junit.Test;

public class FileSystemEntryTest {
    @Test
    public void testAll() {
        Assert.assertEquals(FileSystemEntries.ROOT.buildPath("h","","a"), "h.a");
        Assert.assertEquals(FileSystemEntries.SENSORS.buildPath("a","b","c.d"), "sensors.a.b.c.d");
        Assert.assertEquals(FileSystemEntries.SENSORS_DATA.buildPath("a","b","c.d"), "sensors_data.a.b.c.d");
        Assert.assertEquals(FileSystemEntries.SYSTEM.buildPath("a","b","c.d"), "system.a.b.c.d");
        Assert.assertEquals(FileSystemEntries.SYSTEM_INTERNALS.buildPath("a","b","c.d"), "system.internals.a.b.c.d");
        Assert.assertEquals(FileSystemEntries.SAVEME.buildPath(), "system.internals.saveme");
        Assert.assertEquals(FileSystemEntries.SYSTEM_DATA_IMAGE.buildPath(), "system.data_image");
        Assert.assertEquals(FileSystemEntries.COMMERCIAL_NAME.buildPath("a","b","c.d"), "sensors.a.b.c.d");
        Assert.assertEquals(FileSystemEntries.SENSOR_ID.buildPath("a","b","c.d"), "sensors.a.b.c.d");
        Assert.assertEquals(FileSystemEntries.SENSOR_ID.buildPath("a.b"), "sensors.a.b");
        Assert.assertEquals(FileSystemEntries.LOCATION.buildPath("a","b"), "sensors.a.b.location");
        Assert.assertEquals(FileSystemEntries.DONE_SENDING_MSG.buildPath("a","b"), "sensors.a.b.done");
        Assert.assertEquals(FileSystemEntries.LISTENERS_OF_SENSOR.buildPath("a","b","c.d"), "sensors.a.b.listeners_of_sensor.c.d");
        Assert.assertEquals(FileSystemEntries.APPLICATIONS_DATA.buildPath("a","b","c.d"), "applications_data.a.b.c.d");
    }
    
    @Test(expected = RuntimeException.class)
    public void testBadPaths1() {
        FileSystemEntries.DONE_SENDING_MSG.buildPath("a");
    }
    
    @Test(expected = RuntimeException.class)
    public void testBadPaths2() {
        FileSystemEntries.DONE_SENDING_MSG.buildPath();
    }
    
    @Test(expected = RuntimeException.class)
    public void testBadPaths3() {
        FileSystemEntries.SYSTEM_DATA_IMAGE.buildPath("a","b","c");
    }
}
