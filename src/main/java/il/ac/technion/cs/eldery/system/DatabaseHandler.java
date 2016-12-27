package il.ac.technion.cs.eldery.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import il.ac.technion.cs.eldery.system.exceptions.SensorNotFoundException;
import il.ac.technion.cs.eldery.utils.ListenableList;

/** The API required by ApplicationHandler in order to allow it desired
 * functionalities.
 * @author Sharon
 * @author Elia
 * @author Inbal Zukerman
 * @since Dec 13, 2016 */
public class DatabaseHandler {
    private final Map<String, List<String>> commNames = new HashMap<>();
    private final Map<String, ListenableList<String>> sensors = new HashMap<>();
    private final Map<String, SensorLocation> sensorsLocations = new HashMap<>();

    /** Adds a new sensor to the system, initializing its information table.
     * @param sensorId sensor'd id
     * @param commName sensor's commercial name
     * @param sizeLimit limit of the information table for this sensor */
    public void addSensor(final String sensorId, final String commName, final int sizeLimit) {
        if (commNames.containsKey(commName))
            commNames.get(commName).add(sensorId);
        else
            commNames.put(commName, new ArrayList<>(Arrays.asList(sensorId)));

        sensors.put(sensorId, new ListenableList<String>(sizeLimit));
        sensorsLocations.put(sensorId, SensorLocation.UNDEFINED);
    }

    /** Fetches a list of all the sensor IDs registered to the system with the
     * given commercial name.
     * @param commName commercial name
     * @return list of sensor IDs with the given commercial name */
    public List<String> getSensors(final String commName) {
        return !commNames.containsKey(commName) ? new ArrayList<>() : commNames.get(commName);
    }

    /** Adds a listener to a certain sensor, to be called on
     * <strong>any</strong> update from that sensor
     * @param sensorCommercialName The name of sensor, agreed upon in an
     *        external platform
     * @param notifee The consumer to be called on a change, with the whole
     *        table of the sensor
     * @return The id of the listener, to be used in any future reference to it
     * @throws SensorNotFoundException */
    public String addListener(final String $, final Consumer<String> notifee) throws SensorNotFoundException {
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
        if (!sensors.containsKey(sensorID))
            throw new SensorNotFoundException();

        sensors.get(sensorID).removeListener(listenerId);
    }

    /** Queries the info of a sensor.
     * @param sensorCommercialName The name of sensor, agreed upon in an
     *        external platform
     * @return the most updated data of the sensor, or Optional.empty() if the
     *         request couldn't be completed for any reason */
    public Optional<String> getLastEntryOf(final String sensorID) {
        return Optional.ofNullable(sensors.get(sensorID)).filter(t -> !t.isEmpty()).map(t -> t.get(t.size() - 1));
    }

    /** @param sensorID the ID of the sensor's who's Table is required
     * @return the Table with the information of the wanted sensor
     * @throws SensorNotFoundException */
    public ListenableList<String> getList(final String $) throws SensorNotFoundException {
        if (!sensors.containsKey($))
            throw new SensorNotFoundException();
        return sensors.get($);
    }

    /** Queries the location of a sensor
     * @param sensorId the Id of the sensor it's location to be returned
     * @return the location of the sensor with sensorId
     * @throws SensorNotFoundException */
    public SensorLocation getSensorLocation(final String sensorId) throws SensorNotFoundException {
        if (sensorsLocations.get(sensorId) == null)
            throw new SensorNotFoundException();
        return sensorsLocations.get(sensorId);
    }

    /** Updates the location of a sensor
     * @param sensorId the Id of the sensor it's location to be changed
     * @throws SensorNotFoundException */
    public void setSensorLocation(final String sensorId, final SensorLocation l) throws SensorNotFoundException {
        if (!sensorsLocations.containsKey(sensorId))
            throw new SensorNotFoundException();
        sensorsLocations.remove(sensorId);
        sensorsLocations.put(sensorId, l);
    }

}
