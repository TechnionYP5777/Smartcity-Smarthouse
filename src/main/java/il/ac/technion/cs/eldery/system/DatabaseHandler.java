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
 * @author Elia Traore
 * @author Inbal Zukerman
 * @since Dec 13, 2016 */
public class DatabaseHandler {
    private final Map<String, String> sensorsIdToName = new HashMap<>();
    private final Map<String, List<String>> commNames = new HashMap<>();
    private final Map<String, ListenableList<String>> sensors = new HashMap<>();
    private final Map<String, SensorLocation> sensorsLocations = new HashMap<>();

    private final List<Consumer<String>> newSensorsListeners = new ArrayList<>();

    /** Adds a new sensor to the system, initializing its information List.
     * @param sensorId sensor'd id
     * @param commName sensor's commercial name
     * @param sizeLimit limit of the information List for this sensor */
    public void addSensor(final String sensorId, final String commName, final int sizeLimit) {
        if (commNames.containsKey(commName))
            commNames.get(commName).add(sensorId);
        else
            commNames.put(commName, new ArrayList<>(Arrays.asList(sensorId)));

        sensors.put(sensorId, new ListenableList<String>(sizeLimit));
        sensorsLocations.put(sensorId, SensorLocation.UNDEFINED);
        sensorsIdToName.put(sensorId, commName);

        newSensorsListeners.forEach(consumer -> consumer.accept(sensorId));
    }

    /** Fetches a list of all the sensor IDs registered to the system with the
     * given commercial name.
     * @param commName commercial name
     * @return list of sensor IDs with the given commercial name */
    public List<String> getSensors(final String commName) {
        return !commNames.containsKey(commName) ? new ArrayList<>() : commNames.get(commName);
    }

    /** Returns the commercial name associated with the required sensor.
     * @param sensorId sensor'd id
     * @return commercial name associated with this id
     * @throws SensorNotFoundException if id was not found */
    public String getName(String sensorId) throws SensorNotFoundException {
        if (!sensorsIdToName.containsKey(sensorId))
            throw new SensorNotFoundException();

        return sensorsIdToName.get(sensorId);
    }

    /** Adds a new listener to the list of consumers that will be notified each
     * time a new sensor is registered to the system.
     * @param listener consumer to be called when a sensor is added */
    public void addNewSensorsListener(Consumer<String> listener) {
        newSensorsListeners.add(listener);
    }

    /** Adds a listener to a certain sensor, to be called on
     * <strong>any</strong> update from that sensor
     * @param sensorId The sensorId
     * @param notifee The consumer to be called on a change, with the whole list
     *        of the sensor
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
     * @param sensorId is the id of the sensor which it's listener is to be
     *        removed
     * @param listenerId The id given when the listener was added to the system
     * @throws SensorNotFoundException */
    public void removeListener(final String sensorId, final String listenerId) throws SensorNotFoundException {
        if (!sensors.containsKey(sensorId))
            throw new SensorNotFoundException();

        sensors.get(sensorId).removeListener(listenerId);
    }

    /** Queries the info of a sensor.
     * @param sensorCommercialName The name of sensor, agreed upon in an
     *        external platform
     * @return the most updated data of the sensor, or Optional.empty() if the
     *         request couldn't be completed for any reason */
    public Optional<String> getLastEntryOf(final String sensorId) {
        return Optional.ofNullable(sensors.get(sensorId)).filter(t -> !t.isEmpty()).map(t -> t.get(t.size() - 1));
    }

    /** @param sensorId the ID of the sensor's who's List is required
     * @return the List with the information of the wanted sensor
     * @throws SensorNotFoundException */
    public ListenableList<String> getList(final String sensorId) throws SensorNotFoundException {
        if (!sensors.containsKey(sensorId))
            throw new SensorNotFoundException();
        return sensors.get(sensorId);
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
