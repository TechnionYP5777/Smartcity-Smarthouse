package il.ac.technion.cs.smarthouse.utils;

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

    private final Consumer<Table<String, Integer>> listener = x -> listenerUpdated = true;

    @Test public void initalizationTest() {
        Assert.assertEquals(Table.UNLIMITED_CAPACITY, unlimitedTable.getMaxCapacity());
        Assert.assertEquals(3, limitedTable.getMaxCapacity());
    }

    @Test public void listenersTest() {
        final String listenerId = unlimitedTable.addListener(listener);
        assert listenerId != null;
        unlimitedTable.removeListener(listenerId);
        assert unlimitedTable != null;
    }

    @Test public void addEntryTest() {
        Assert.assertNull(unlimitedTable.getLastEntry());
        unlimitedTable.addEntry(info1);
        Assert.assertEquals(info1, unlimitedTable.getLastEntry());

        assert !listenerUpdated;
        unlimitedTable.addListener(listener);
        unlimitedTable.addEntry(info2);
        assert listenerUpdated;

        listenerUpdated = false;
        assert !listenerUpdated;
        limitedTable.addListener(listener);
        limitedTable.addEntry(info1);
        assert listenerUpdated;

    }

}
