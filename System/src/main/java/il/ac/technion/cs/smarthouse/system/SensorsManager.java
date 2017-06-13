package il.ac.technion.cs.smarthouse.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.exceptions.SensorNotFoundException;

/**
 * @author Elia Traore
 * @author Inbal Zukerman
 * @since Dec 13, 2016
 */
public class SensorsManager {

    private static Logger log = LoggerFactory.getLogger(SensorsManager.class);

    private final List<String> sensors = new ArrayList<>();
    private final Map<String, String> sensorsLocations = new HashMap<>();

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
        sensorsLocations.put(sensorId, "UNDEFINED");

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
    public String getSensorLocation(final String sensorId) throws SensorNotFoundException {
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
    public void setSensorLocation(final String sensorId, final String l) throws SensorNotFoundException {
        if (!sensorsLocations.containsKey(sensorId)) {
            log.error("Sensor was not found");
            throw new SensorNotFoundException(sensorId);
        }

        sensorsLocations.put(sensorId, l);

    }

}
