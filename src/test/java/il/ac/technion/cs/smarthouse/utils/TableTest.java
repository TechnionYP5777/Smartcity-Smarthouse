package il.ac.technion.cs.smarthouse.utils;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.utils.exceptions.OutOfTableLimit;

/** @author Inbal Zukerman
 * @since 15.12.2016 */
public class TableTest {

    private final Map<String, Integer> info1 = new HashMap<>();
    private final Map<String, String> info2 = new HashMap<>();

    private final Map<String, Integer> moreInfo = new HashMap<>();
    private final Map<String, Integer> moreInfo2 = new HashMap<>();

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

        limitedTable.changeMaxCapacity(1);
        Assert.assertEquals(1, limitedTable.getCurrentCapacity());
        Assert.assertEquals(moreInfo2, limitedTable.getLastEntry());

        unlimitedTable.changeMaxCapacity(3);
        Assert.assertEquals(3, unlimitedTable.getMaxCapacity());

        unlimitedTable.disableCapacityLimit();
        Assert.assertEquals(Table.UNLIMITED_CAPACITY, unlimitedTable.getMaxCapacity());
    }

    @Test public void getLastKEntriesTest() {

        Assert.assertEquals(0, limitedTable.getCurrentCapacity());
        Assert.assertNull(limitedTable.receiveKLastEntries(0));
        limitedTable.addEntry(info1);
        limitedTable.addEntry(moreInfo);
        limitedTable.addEntry(moreInfo2);

        final Table<String, Integer> twoEntries = limitedTable.receiveKLastEntries(2);
        Assert.assertEquals(2, twoEntries.getMaxCapacity());
        Assert.assertEquals(2, twoEntries.getCurrentCapacity());
        Assert.assertEquals(moreInfo2, twoEntries.getLastEntry());

        final Table<String, Integer> allEntries = limitedTable.receiveKLastEntries(5);
        Assert.assertEquals(5, allEntries.getMaxCapacity());
        Assert.assertEquals(3, allEntries.getCurrentCapacity());

    }

    @Test public void getLastEntryAtColTest() {
        Assert.assertNull(unlimitedTable.getLastEntryAtCol("A"));
        info1.put("A", 1);
        info1.put("B", 2);
        limitedTable.addEntry(info1);

        Assert.assertEquals(2, (int) limitedTable.getLastEntryAtCol("B"));
    }

    @Test public void getTest() {

        try {
            Assert.assertNull(unlimitedTable.get(1, "A"));
        } catch (final OutOfTableLimit e) {
            assert false; // should never get here
        }

        info1.put("A", 1);
        info1.put("B", 2);
        moreInfo.put("X", 3);
        moreInfo.put("Y", 4);
        limitedTable.addEntry(info1);
        limitedTable.addEntry(moreInfo);

        try {
            Assert.assertEquals(1, (int) limitedTable.get(Table.OLDEST_DATA_INDEX, "A"));
            Assert.assertEquals(2, (int) limitedTable.get(Table.OLDEST_DATA_INDEX, "B"));
            Assert.assertEquals(3, (int) limitedTable.get(1, "X"));
        } catch (final OutOfTableLimit e) {
            assert false; // should never get here
        }
    }
}
