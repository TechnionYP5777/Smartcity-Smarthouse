package il.ac.technion.cs.smarthouse.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/** @param <T> type of the column names of this table
 * @param <S> type of the data stored in this table
 * @author Sharon
 * @since 17.12.16 */
public class ListenableTable<T, S> extends Table<T, S> {
    private final Map<String, Consumer<Table<T, S>>> listeners = new HashMap<>();

    public ListenableTable() {}

    public ListenableTable(final int maxCapacity) {
        super(maxCapacity);
    }

    /** Adds a new listener to the table
     * @param listener listener to be added
     * @return id of the new listener */
    public String addListener(final Consumer<Table<T, S>> listener) {
        final String $ = UuidGenerator.GenerateUniqueIDstring();
        listeners.put($, listener);

        return $;
    }

    /** Removes a listener from the table
     * @param id id of the listener to be removed */
    public void removeListener(final String id) {
        listeners.remove(id);
    }

    @Override public void addEntry(final Map<T, S> info) {
        super.addEntry(info);

        listeners.values().forEach(listener -> listener.accept(receiveKLastEntries(1)));
    }
}
