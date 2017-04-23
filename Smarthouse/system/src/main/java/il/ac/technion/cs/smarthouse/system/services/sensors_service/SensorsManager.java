package il.ac.technion.cs.smarthouse.system.services.sensors_service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.exceptions.SensorNotFoundException;
import il.ac.technion.cs.smarthouse.system.services.Service;

/** A service API for the sensors.
 * <p>
 * This service allows the developer to get {@link SensorApi}'s for sensors by
 * their commercial names
 * @author RON
 * @since 02-04-2017 */
public final class SensorsManager extends Service {

    private static Logger log = LoggerFactory.getLogger(SensorsManager.class);

    public SensorsManager(final SystemCore $) {
        super($);
    }

    /** Ask for a list of sensors registered by a commercial name.
     * @param sensorDataClass a {@link SensorData} class that will define the
     *        sensor's data structure. Note that all sensors registered with the
     *        {@link SensorData} class given commercial names, should be able to
     *        interact with the same
     * @param commercialNames a list of possible commercial names
     * @return a list of {@link SensorApi} that are registered with one of the
     *         given commercial names
     * @throws SensorNotFoundException if no sensor was found with the given
     *         commercial names */
    public <T extends SensorData> List<SensorApi<T>> getAllSensors(final Class<T> sensorDataClass, String... commercialNames)
            throws SensorNotFoundException {
        List<SensorApi<T>> l = new ArrayList<>();
        
        for (String sensorCommercialName : commercialNames)
            for (String sensorId : systemCore.databaseHandler.getSensors(sensorCommercialName))
                l.add(new SensorApi<>(systemCore, sensorId, sensorDataClass));

        if (l.isEmpty()) {
            log.info("No sensor was found with the commercial names: " + String.join(", ", commercialNames));
            throw new SensorNotFoundException("COMM_NAME"); // TODO: maybe change the exception class. @RonGatenio
        }

        return l;
    }

    /** Ask for a the first sensor in the list of sensors registered by a
     * commercial name.
     * @param sensorDataClass a {@link SensorData} class that will define the
     *        sensor's data structure. Note that all sensors registered with the
     *        {@link SensorData} class given commercial names, should be able to
     *        interact with the same
     * @param commercialNames a list of possible commercial names
     * @return the first {@link SensorApi} that is registered with one of the
     *         given commercial names
     * @throws SensorNotFoundExceptionif no sensor was found with the given
     *         commercial names
     * @see SensorsManager#getAllSensors(Class, String...) */
    public <T extends SensorData> SensorApi<T> getDefaultSensor(final Class<T> sensorDataClass, String... commercialNames)
            throws SensorNotFoundException {
        List<SensorApi<T>> l = getAllSensors(sensorDataClass, commercialNames);
        return l.isEmpty() ? null : l.get(0);
    }
}
