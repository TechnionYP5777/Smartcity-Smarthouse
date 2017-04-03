package il.ac.technion.cs.smarthouse.utils;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.utils.ListenableList;

/** @author Inbal Zukerman
 * @since Dec 26, 2016 */
public class ListenableListTest {

    private final ListenableList<String> unlimitedList = new ListenableList<>();
    private final ListenableList<String> limitedList = new ListenableList<>(3);

    @Test public void initializationTest() {
        Assert.assertEquals(3, limitedList.getMaxCapacity());
        Assert.assertEquals(ListenableList.UNLIMITED_CAPACITY, unlimitedList.getMaxCapacity());

    }

    @Test public void addDataTest() {
        unlimitedList.add("A");
        unlimitedList.add("B");

        limitedList.add("A");
        limitedList.add("B");
        limitedList.add("C");
        limitedList.add("D");

        Assert.assertEquals(2, unlimitedList.size());
        Assert.assertEquals(3, limitedList.size());
        Assert.assertEquals("B", limitedList.get(ListenableList.OLDEST_DATA_INDEX));
        Assert.assertEquals("D", limitedList.get(limitedList.size() - 1));

        unlimitedList.add(2, "X");
        Assert.assertEquals("X", unlimitedList.getLastEntry());

        unlimitedList.add(2, "Y");
        Assert.assertEquals(4, unlimitedList.size());
        Assert.assertNotEquals("Y", unlimitedList.getLastEntry());
        Assert.assertEquals("X", unlimitedList.getLastEntry());
        Assert.assertEquals("Y", unlimitedList.get(2));

        limitedList.add(0, "X");
        Assert.assertEquals("X", limitedList.get(ListenableList.OLDEST_DATA_INDEX));
        limitedList.add(4, "OH NO");
        Assert.assertEquals(3, limitedList.size());
        Assert.assertNotEquals("OH NO", limitedList.getLastEntry());

    }

    @Test public void capacityTest() {
        unlimitedList.add("A");
        unlimitedList.add("B");
        unlimitedList.add("C");
        unlimitedList.add("D");

        unlimitedList.changeMaxCapacity(2);
        Assert.assertEquals(2, unlimitedList.getMaxCapacity());
        Assert.assertEquals(2, unlimitedList.size());
        Assert.assertEquals("C", unlimitedList.get(ListenableList.OLDEST_DATA_INDEX));
        Assert.assertEquals("D", unlimitedList.get(unlimitedList.size() - 1));

        unlimitedList.disableCapacityLimit();
        Assert.assertEquals(ListenableList.UNLIMITED_CAPACITY, unlimitedList.getMaxCapacity());
        unlimitedList.add("A");
        Assert.assertEquals("C", unlimitedList.get(ListenableList.OLDEST_DATA_INDEX));
        Assert.assertEquals("A", unlimitedList.get(unlimitedList.size() - 1));

    }

    @Test public void getLastKEntriesTest() {

        Assert.assertEquals(0, limitedList.size());
        Assert.assertNull(limitedList.getLastKEntries(0));
        limitedList.add("info1");
        limitedList.add("moreInfo");
        limitedList.add("moreInfo2");

        final List<String> twoEntries = limitedList.getLastKEntries(2);
        Assert.assertEquals(2, twoEntries.size());
        Assert.assertEquals("moreInfo2", twoEntries.get(twoEntries.size() - 1));

        Assert.assertEquals(3, limitedList.getLastKEntries(3).size());
    }
}
