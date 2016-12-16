package il.ac.technion.cs.eldery.system.sensors;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import il.ac.technion.cs.eldery.utils.Generator;
import il.ac.technion.cs.eldery.utils.Table;

/** @author Inbal Zukerman
 * @author Sharon
 * @since 16.12.2016 */
public class SensorInfo {
    private Table<Object, Object> information = new Table<>();
    private Map<String, Consumer<Table<Object, Object>>> listeners = new HashMap<>();

    /** Creates SensorInfo without information capacity limit. */
    public SensorInfo() {}

    /** @param infoLimit - information capacity limit to enforce */
    public SensorInfo(final int infoLimit) {
        this.information.changeMaxCapacity(infoLimit);
    }

    /** Initializes a new sensor info object with the given information.
     * @param information observation the object will be initialized with */
    private SensorInfo(final Table<Object, Object> information) {
        this.information = information;
    }

    /** Adds a new listener to changes made to the observations table.
     * @param listener listener to be invoked when changed are made
     * @return id of the added listener */
    public String addListener(final Consumer<Table<Object, Object>> listener) {
        final String $ = Generator.GenerateUniqueIDstring();
        listeners.put($, listener);
        return $;
    }

    /** Removes a listener from the listeners list.
     * @param listenerId id of the listener to remove */
    public void removeListener(final String listenerId) {
        listeners.remove(listenerId);
    }

    /** Adds a new entry to the table of observations.
     * @param record entry to add */
    public void addRecord(final HashMap<Object, Object> record) {
        information.addEntry(record);
    }

    /** @return max capacity of the observations table */
    public int getInfoLimit() {
        return information.getMaxCapacity();
    }

    /** @return current size of the observations table */
    public int getNumRecords() {
        return information.getCurrentCapacity();
    }

    /** Sets a new limit to the observations table.
     * @param newLimit new limit */
    public void changeInfoLimit(final int newLimit) {
        information.changeMaxCapacity(newLimit);
    }

    /** Changes the max size of the table to be unlimited. */
    public void unlimitInfo() {
        information.disableCapacityLimit();
    }

    /** Returns a sensor info object containing only the last k observations.
     * @param k amount of observations that remain
     * @return new sensor info object */
    public SensorInfo getLastKRecords(final int k) {
        return new SensorInfo(information.receiveKLastEntries(k));
    }

    /** @return last observations entry */
    public Map<Object, Object> getLastRecord() {
        return information.getLastEntry();
    }

    /** @param colName observation name
     * @return last observation of the given type */
    public Object getLastRecordAtCol(final Object colName) {
        return information.getLastEntryAtCol(colName);
    }
}
