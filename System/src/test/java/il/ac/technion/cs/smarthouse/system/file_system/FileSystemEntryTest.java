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
        Assert.assertEquals(FileSystemEntries.SAVEME.buildPath("a","b","c.d"), "system.internals.saveme.a.b.c.d");
        Assert.assertEquals(FileSystemEntries.SYSTEM_DATA_IMAGE.buildPath("a","b","c.d"), "system.data_image.a.b.c.d");
        Assert.assertEquals(FileSystemEntries.COMMERCIAL_NAME.buildPath("a","b","c.d"), "sensors.a.b.c.d");
        Assert.assertEquals(FileSystemEntries.SENSOR_ID.buildPath("a","b","c.d"), "sensors.a.b.c.d");
        Assert.assertEquals(FileSystemEntries.SENSOR_ID.buildPath("a.b"), "sensors.a.b");
        Assert.assertEquals(FileSystemEntries.LOCATION.buildPath("a","b","c.d"), "sensors.a.b.location.c.d");
        Assert.assertEquals(FileSystemEntries.DONE_SENDING_MSG.buildPath("a","b","c.d"), "sensors.a.b.done.c.d");
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
}
