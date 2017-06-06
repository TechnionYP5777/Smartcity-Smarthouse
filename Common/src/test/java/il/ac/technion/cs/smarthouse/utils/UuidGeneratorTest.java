package il.ac.technion.cs.smarthouse.utils;

import org.junit.Assert;
import org.junit.Test;

public class UuidGeneratorTest {
    @Test
    public void mainTest() {
        Assert.assertNotEquals(UuidGenerator.GenerateUniqueIDstring(), UuidGenerator.generateUniqueID() + "");
        Assert.assertNotEquals(UuidGenerator.GenerateUniqueIDstring(), UuidGenerator.GenerateUniqueIDstring());
        Assert.assertNotEquals(UuidGenerator.GenerateUniqueIDstring(), UuidGenerator.GenerateUniqueIDstring());
        Assert.assertNotEquals(UuidGenerator.GenerateUniqueIDstring(), UuidGenerator.GenerateUniqueIDstring());
        Assert.assertNotEquals(UuidGenerator.GenerateUniqueIDstring(), UuidGenerator.GenerateUniqueIDstring());
        Assert.assertNotEquals(UuidGenerator.GenerateUniqueIDstring(), UuidGenerator.GenerateUniqueIDstring());
    }
}
