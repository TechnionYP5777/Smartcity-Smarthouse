package il.ac.technion.cs.smarthouse.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/** @author Inbal Zukerman
 * @since Dec 25, 2016 */
public class ListenableList<T> extends ArrayList<T> {
    private static final long serialVersionUID = -0x61DFA4D7D65FF9FCL;

    public static final int UNLIMITED_CAPACITY = -1;
    public static final int OLDEST_DATA_INDEX = 0;

    private final Map<String, Consumer<T>> listeners = new HashMap<>();
    boolean limitedSize;
    private int maxCapacity;

    /** Initializes a new ListenableList without a capacity limit */
    public ListenableList() {
        this.limitedSize = false;

    }

    /** Initializes a new ListenableList with a capacity limit
     * @param maxCapacity - the capacity limit required */
    public ListenableList(final int maxCapacity) {
        this.limitedSize = true;
        this.maxCapacity = maxCapacity;
    }

    /** @return the max capacity limit if exists, UNLIMITED_CAPACITY
     *         otherwise */
    public int getMaxCapacity() {
        return limitedSize ? maxCapacity : UNLIMITED_CAPACITY;
    }

    /** @param newCapacity - the new capacity limit to enforce. */
    public void changeMaxCapacity(final int newCapacity) {
        if (!limitedSize)
            limitedSize = true;
        if (super.size() >= newCapacity)
            super.subList(0, super.size() - newCapacity).clear();
        maxCapacity = newCapacity;
    }

    /** Disable the capacity limitation */
    public void disableCapacityLimit() {
        limitedSize = false;
    }

    @Override public boolean add(final T e) {

        listeners.values().forEach(listener -> listener.accept(e));
        if (this.limitedSize && super.size() == this.maxCapacity)
            super.remove(0);

        return super.add(e);
    }

    /** this method fails if the list is limited the given index is bigger than
     * the list limit. */
    @Override public void add(final int index, final T element) {
        if (limitedSize && index > this.maxCapacity - 1)
            return;
        listeners.values().forEach(listener -> listener.accept(element));
        if (this.limitedSize && super.size() == this.getMaxCapacity())
            super.remove(0);
        super.add(index, element);
    }

    /** Adds a new listener to the List
     * @param listener listener to be added
     * @return id of the new listener */
    public String addListener(final Consumer<T> listener) {
        final String $ = UuidGenerator.GenerateUniqueIDstring();
        listeners.put($, listener);

        return $;
    }

    /** Removes a listener from the List
     * @param id id of the listener to be removed */
    public void removeListener(final String id) {
        listeners.remove(id);
    }

    public T getLastEntry() {
        return super.isEmpty() ? null : get(size() - 1);
    }

    public List<T> getLastKEntries(final int k) {
        if (super.isEmpty())
            return null;

        final List<T> $ = new ArrayList<>();
        final int position = k > super.size() ? 0 : super.size() - k;
        for (int ¢ = position; ¢ < super.size(); ++¢)
            $.add(super.get(¢));
        return $;

    }
}
