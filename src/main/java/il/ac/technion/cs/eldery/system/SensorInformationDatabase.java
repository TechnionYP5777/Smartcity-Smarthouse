package il.ac.technion.cs.eldery.system;

import java.util.*;

import il.ac.technion.cs.eldery.utils.*;

/** @author Inbal Zukerman
 * @since 9.12.2016 */
public class SensorInformationDatabase<L, R> {
    private final String sensorId;
    private final ArrayList<Tuple<L, R>> information = new ArrayList<>();
    private int maxCapacity;

    /** @param maxCapacity - the maximal capacity required for this database. If
     *        maxCapacity is 0, the database will be initialized with
     *        maxCapacity=1. This method creates the new database for
     *        information from the sensor */
    public SensorInformationDatabase(final String sensorId, final int maxCapacity) {
        this.sensorId = sensorId;
        this.maxCapacity = maxCapacity <= 0 ? 1 : maxCapacity;
    }

    /** @param info- a tuple representing information received from a sensor
     *        This method inserts new information to the database.If needed,
     *        after removing the oldest information received */
    public void addInfo(final Tuple<L, R> info) {
        if (this.information.size() == this.maxCapacity)
            this.information.remove(0);
        this.information.add(info);
    }

    public int getMaxCapacity() {
        return this.maxCapacity;
    }

    public int getCurrentCapacity() {
        return this.information.size();
    }

    /** @param newCapacity This method updates the capacity of the sensor's
     *        database */
    public void changeMaxCapacity(final int newCapacity) {
        if (this.information.size() >= newCapacity) {
            final int cutOff = this.information.size() - newCapacity;
            for (int ¢ = 0; ¢ < cutOff; ++¢)
                this.information.remove(0);
        }
        this.maxCapacity = newCapacity;
    }

    public ArrayList<Tuple<L, R>> recievceLastUpdates(final int numOfUpdates) {
        if (numOfUpdates <= 0)
            return null;
        final ArrayList<Tuple<L, R>> $ = new ArrayList<>();
        final int position = numOfUpdates > this.information.size() ? 0 : this.information.size() - numOfUpdates;
        for (int ¢ = position; ¢ < this.information.size(); ++¢)
            $.add(this.information.get(¢));
        return $;
    }

    public Tuple<L, R> getLastUpdate() {
        return this.information.isEmpty() ? null : this.information.get(this.information.size() - 1);
    }

    public boolean doesExists(final Tuple<L, R> info) {
        return this.information.indexOf(info) != -1;
    }

    public String getSensorId() {
        return sensorId;
    }
}
