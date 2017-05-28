package il.ac.technion.cs.smarthouse.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.parse4j.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.database.DatabaseManager;
import il.ac.technion.cs.smarthouse.networking.messages.MessageType;
import il.ac.technion.cs.smarthouse.system.exceptions.SensorNotFoundException;

/**
 * The API required by ApplicationHandler in order to allow it desired
 * functionalities.
 * 
 * @author Sharon
 * @author Elia Traore
 * @author Inbal Zukerman
 * @since Dec 13, 2016
 */
public class DatabaseHandler {  

    private static Logger log = LoggerFactory.getLogger(DatabaseHandler.class);

    private final List<String> sensors = new ArrayList<>();
    private final Map<String, SensorLocation> sensorsLocations = new HashMap<>();
    private final DatabaseManager dbManager = new DatabaseManager(); // TODO: inbal, should be here?
    
    /**
     * Adds a new sensor to the system, initializing its information List.
     * 
     * @param sensorId
     *            sensor'd id
     * @param commName
     *            sensor's commercial name
     * @param sizeLimit
     *            limit of the information List for this sensor
     */
    public void addSensor(final String sensorId) {

        sensors.add(sensorId);
        sensorsLocations.put(sensorId, SensorLocation.UNDEFINED);

    }

    public Boolean sensorExists(final String id) {

        return sensors.contains(id);
    }

    /**
     * Queries the location of a sensor
     * 
     * @param sensorId
     *            the Id of the sensor it's location to be returned
     * @return the location of the sensor with sensorId
     * @throws SensorNotFoundException
     */
    public SensorLocation getSensorLocation(final String sensorId) throws SensorNotFoundException {
        if (sensorsLocations.get(sensorId) == null) {
            log.error("Sensor was not found");
            throw new SensorNotFoundException(sensorId);
        }
        return sensorsLocations.get(sensorId);
    }

    /**
     * Updates the location of a sensor
     * 
     * @param sensorId
     *            the Id of the sensor it's location to be changed
     * @throws SensorNotFoundException
     */
    public void setSensorLocation(final String sensorId, final SensorLocation l) throws SensorNotFoundException {
        if (!sensorsLocations.containsKey(sensorId)) {
            log.error("Sensor was not found");
            throw new SensorNotFoundException(sensorId);
        }

        sensorsLocations.put(sensorId, l);

    }

    public void handleUpdateMessage(final String message) {
        try {
            final String[] parts = message.split(Dispatcher.SEPARATOR);
            dbManager.addInfo(InfoType.SENSOR,
                            parts[0].replace((MessageType.UPDATE.toString() + Dispatcher.DELIMITER).toLowerCase(), ""),
                            parts[1]);
        } catch (final ParseException e) {
            log.error("Update message was not handled properly", e);

        }
        // TODO: inbal

    }

}
