package il.ac.technion.cs.eldery.utils;

import org.junit.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/** @author Inbal Zukerman
 * @since Dec 27, 2016 */

public class ListenableTableTest {

    private ListenableTable<String, Integer> data = new ListenableTable<>();
    private final Map<String, Integer> info1 = new HashMap<>();
    private final Map<String, Integer> info2 = new HashMap<>();

    private boolean listenerUpdated;

    private Consumer<Table<String, Integer>> listener = (x) -> {
        listenerUpdated = true;
    };

    @Test public void listenersTest() {
        String listenerId = this.data.addListener(listener);
        Assert.assertNotNull(listenerId);
        this.data.removeListener(listenerId);
        Assert.assertNotNull(data);
    }

    @Test public void addEntryTest() {
        Assert.assertNull(this.data.getLastEntry());
        this.data.addEntry(info1);
        Assert.assertEquals(info1, this.data.getLastEntry());

        Assert.assertFalse(listenerUpdated);
        String listenerId = this.data.addListener(listener);
        this.data.addEntry(info2);
        Assert.assertTrue(listenerUpdated);

    }

}
