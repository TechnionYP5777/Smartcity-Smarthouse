package il.ac.technion.cs.smarthouse.system.database;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.utils.UuidGenerator;

public class LocalSaverTest {
    private static final String filename = "blablaTest_" + UuidGenerator.GenerateUniqueIDstring() + ".data";

    @After
    public void end() {
        File f = new File(filename);
        if (f.exists())
            f.delete();
    }

    @Test
    public void mainTest() {
        final String data = "Hello " + UuidGenerator.GenerateUniqueIDstring();
        LocalSaver.saveData(data, filename);
        Assert.assertEquals(LocalSaver.readData(filename), data);
    }
}
