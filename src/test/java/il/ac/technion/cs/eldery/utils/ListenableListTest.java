package il.ac.technion.cs.eldery.utils;

import org.junit.Assert;
import org.junit.Test;

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

}
