package il.ac.technion.cs.smarthouse.utils;

import java.util.ArrayList;
import java.util.Map;

import il.ac.technion.cs.smarthouse.utils.exceptions.OutOfTableLimit;

/** @author Inbal Zukerman
 * @since 15.12.2016 */
public class Table<L, R> {
    public static final int UNLIMITED_CAPACITY = -1;
    public static final int OLDEST_DATA_INDEX = 0;

    private final ArrayList<Map<L, R>> data = new ArrayList<>();
    boolean limitedSize;
    private int maxCapacity;

    /** Initializes a new Table without capacity limit */
    public Table() {
        limitedSize = false;
    }

    /** Initializes a new Table with a capacity limit
     * @param maxCapacity - the capacity limit required */
    public Table(final int maxCapacity) {
        limitedSize = true;
        this.maxCapacity = maxCapacity <= 0 ? 1 : maxCapacity;
    }

    /** Adds an entry to the table
     * @param info - the new entry to add */
    public void addEntry(final Map<L, R> info) {
        if (limitedSize && data.size() == maxCapacity)
            data.remove(0);
        data.add(info);
    }

    /** @return the max capacity limit if exists, UNLIMITED_CAPACITY
     *         otherwise */
    public int getMaxCapacity() {
        return limitedSize ? maxCapacity : UNLIMITED_CAPACITY;
    }

    /** @return the current capacity of the table (it's size) */
    public int getCurrentCapacity() {
        return data.size();
    }

    /** @param newCapacity - the new capacity limit to enforce. */
    public void changeMaxCapacity(final int newCapacity) {
        if (!limitedSize)
            limitedSize = true;
        if (data.size() >= newCapacity) {
            final int cutOff = data.size() - newCapacity;
            for (int ¢ = 0; ¢ < cutOff; ++¢)
                data.remove(0);
        }
        maxCapacity = newCapacity;
    }

    /** Disable the capacity limitation */
    public void disableCapacityLimit() {
        limitedSize = false;
    }

    /** @param numOfEntries- how many entries (from the newest to oldest) to
     *        return
     * @return A new table with the required entries */
    public Table<L, R> receiveKLastEntries(final int numOfEntries) {
        if (numOfEntries <= 0)
            return null;
        final Table<L, R> $ = new Table<>(numOfEntries);
        final int position = numOfEntries > data.size() ? 0 : data.size() - numOfEntries;
        for (int ¢ = position; ¢ < data.size(); ++¢)
            $.addEntry(data.get(¢));
        return $;
    }

    /** @return the last entry added to the table */
    public Map<L, R> getLastEntry() {
        return data.isEmpty() ? null : data.get(data.size() - 1);
    }

    /** @param key - the key for the value wanted
     * @return the value co-respond with the key of the last entry which is
     *         required or null if it doesn't exist */
    public R getLastEntryAtCol(final L key) {
        return data.isEmpty() ? null : data.get(data.size() - 1).get(key);
    }

    /** @param row - number of row, starts at index 0. index 0 is the OLDEST
     *        data
     * @param col - which column to retrieved data from
     * @return the R object in the row and column of the parameters
     * @throws OutOfTableLimit */
    public R get(final int row, final L col) throws OutOfTableLimit {
        if (data.isEmpty())
            return null;
        if (row < 0 || this.limitedSize && row > this.maxCapacity)
            throw new OutOfTableLimit();
        return this.data.get(row).get(col);
    }
}
