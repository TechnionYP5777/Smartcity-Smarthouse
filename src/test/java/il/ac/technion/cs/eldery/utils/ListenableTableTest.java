package il.ac.technion.cs.eldery.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.junit.Assert;
import org.junit.Test;

/** @author Inbal Zukerman
 * @since Dec 27, 2016 */

public class ListenableTableTest {

    private final ListenableTable<String, Integer> unlimitedTable = new ListenableTable<>();
    private final ListenableTable<String, Integer> limitedTable = new ListenableTable<>(3);
    private final Map<String, Integer> info1 = new HashMap<>();
    private final Map<String, Integer> info2 = new HashMap<>();

    private boolean listenerUpdated;

    private final Consumer<Table<String, Integer>> listener = (x) -> {
        listenerUpdated = true;
    };

    @Test public void initalizationTest() {
        Assert.assertEquals(Table.UNLIMITED_CAPACITY, unlimitedTable.getMaxCapacity());
        Assert.assertEquals(3, limitedTable.getMaxCapacity());
    }

    @Test public void listenersTest() {
        final String listenerId = unlimitedTable.addListener(listener);
        Assert.assertNotNull(listenerId);
        unlimitedTable.removeListener(listenerId);
        Assert.assertNotNull(unlimitedTable);
    }

    @Test public void addEntryTest() {
        Assert.assertNull(unlimitedTable.getLastEntry());
        unlimitedTable.addEntry(info1);
        Assert.assertEquals(info1, unlimitedTable.getLastEntry());

        Assert.assertFalse(listenerUpdated);
        unlimitedTable.addListener(listener);
        unlimitedTable.addEntry(info2);
        Assert.assertTrue(listenerUpdated);

        listenerUpdated = false;
        Assert.assertFalse(listenerUpdated);
        limitedTable.addListener(listener);
        limitedTable.addEntry(info1);
        Assert.assertTrue(listenerUpdated);

    }

}
