package il.ac.technion.cs.eldery.utils;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Inbal Zukerman
 * @since 15.12.2016
 *
 */
public class TableTest {

    private final HashMap<String, Integer> info1 = new HashMap<>();
    private final HashMap<String, String> info2 = new HashMap<>();

    private final HashMap<String, Integer> moreInfo = new HashMap<>();
    private final HashMap<String, Integer> moreInfo2 = new HashMap<>();

    private final Table<String, Integer> limitedTable = new Table<>(5);
    private final Table<String, String> unlimitedTable = new Table<>();


    @Test public void initializationTest() {
        Assert.assertEquals(5, limitedTable.getMaxCapacity());
        Assert.assertNull(limitedTable.getLastEntry());
        Assert.assertEquals(Table.UNLIMITED_CAPACITY, unlimitedTable.getMaxCapacity());
        Assert.assertNull(unlimitedTable.getLastEntry());
    }

    @Test public void addInfoTest() {
        Assert.assertNull(limitedTable.getLastEntry());
        Assert.assertNull(unlimitedTable.getLastEntry());

        limitedTable.addEntry(info1);
        Assert.assertEquals(info1, limitedTable.getLastEntry());
        unlimitedTable.addEntry(info2);
        Assert.assertEquals(info2, unlimitedTable.getLastEntry());

    }

    @Test public void capacityTest() {
        Assert.assertEquals(5, limitedTable.getMaxCapacity());
        Assert.assertEquals(Table.UNLIMITED_CAPACITY, unlimitedTable.getMaxCapacity());

        limitedTable.changeMaxCapacity(2);
        Assert.assertEquals(2, limitedTable.getMaxCapacity());
        limitedTable.addEntry(info1);

        limitedTable.addEntry(moreInfo);
        Assert.assertEquals(2, limitedTable.getCurrentCapacity());

        limitedTable.addEntry(moreInfo2);
        Assert.assertEquals(2, limitedTable.getCurrentCapacity());
        Assert.assertEquals(moreInfo2, limitedTable.getLastEntry());

        unlimitedTable.changeMaxCapacity(3);
        Assert.assertEquals(3, unlimitedTable.getMaxCapacity());

        unlimitedTable.disableCapacityLimit();
        Assert.assertEquals(Table.UNLIMITED_CAPACITY, unlimitedTable.getMaxCapacity());
    }

    @Test public void getLastKEntriesTest() {
        
        limitedTable.addEntry(info1);
        limitedTable.addEntry(moreInfo);
        limitedTable.addEntry(moreInfo2);
        
        Table<String, Integer> twoEntries = limitedTable.receiveKLastEntries(2);
        Assert.assertEquals(2, twoEntries.getMaxCapacity());
        Assert.assertEquals(2, twoEntries.getCurrentCapacity());
        Assert.assertEquals(moreInfo2, twoEntries.getLastEntry());

    }

}
