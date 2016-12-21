package il.ac.technion.cs.eldery.system;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import il.ac.technion.cs.eldery.system.exceptions.SensorNotFoundException;
import il.ac.technion.cs.eldery.utils.ListenableTable;
import il.ac.technion.cs.eldery.utils.Table;

/** The API required by ApplicationHandler in order to allow it desired
 * functionalities.
 * @author Elia
 * @author Inbal Zukerman
 * @since Dec 13, 2016 */
public class DatabaseHandler {

    private final Map<String, ListenableTable<String, String>> sensors = new HashMap<>();

    public void addSensor(final String sensorId, final int sizeLimit) {
        sensors.put(sensorId, new ListenableTable<String, String>(sizeLimit));
    }

    /** Adds a listener to a certain sensor, to be called on
     * <strong>any</strong> update from that sensor
     * @param sensorCommercialName The name of sensor, agreed upon in an
     *        external platform
     * @param notifee The consumer to be called on a change, with the whole
     *        table of the sensor
     * @return The id of the listener, to be used in any future reference to it
     * @throws SensorNotFoundException */
    public String addListener(final String $, final Consumer<Table<String, String>> notifee) throws SensorNotFoundException {
        try {
            return sensors.get($).addListener(notifee);
        } catch (@SuppressWarnings("unused") final Exception e) {
            throw new SensorNotFoundException();
        }
    }

    /** Remove a previously added listener
     * @param sensorID is the id of the sensor which it's listener is to be
     *        removed
     * @param listenerId The id given when the listener was added to the system
     * @throws SensorNotFoundException */
    public void removeListener(final String sensorID, final String listenerId) throws SensorNotFoundException {
        try {
            sensors.get(sensorID).removeListener(listenerId);
        } catch (@SuppressWarnings("unused") final Exception e) {
            throw new SensorNotFoundException();
        }
    }

    /** Queries the info of a sensor.
     * @param sensorCommercialName The name of sensor, agreed upon in an
     *        external platform
     * @return the most updated data of the sensor, or Optional.empty() if the
     *         request couldn't be completed for any reason */
    public Optional<Table<String, String>> getLastEntryOf(final String sensorID) {
        return Optional.ofNullable(sensors.get(sensorID)).map(t -> t.receiveKLastEntries(1));
    }

    /** @param sensorID the ID of the sensor's who's Table is required
     * @return the Table with the information of the wanted sensor
     * @throws SensorNotFoundException */
    public Table<String, String> getTable(final String $) throws SensorNotFoundException {
        try {
            return sensors.get($);
        } catch (@SuppressWarnings("unused") final Exception e) {
            throw new SensorNotFoundException();
        }
    }
}
