package il.ac.technion.cs.smarthouse.system.services.sensors_service;

import java.util.ArrayList;
import java.util.List;

import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.services.Service;

/**
 * A service API for the sensors.
 * <p>
 * This service allows the developer to get {@link SensorApiImpl}'s for sensors
 * by their commercial names
 * 
 * @author RON
 * @since 02-04-2017
 */
public final class SensorsService extends Service {

    public SensorsService(final SystemCore $) {
        super($);
    }

    /**
     * Ask for a list of sensors registered by a commercial name.
     * 
     * @param sensorDataClass
     *            a {@link SensorData} class that will define the sensor's data
     *            structure. Note that all sensors registered with the
     *            {@link SensorData} class given commercial names, should be
     *            able to interact with the same
     * @param commercialName
     *            a commercial name
     * @return a {@link SensorApiImpl} that are registered with 
     *         the given commercial name and alias
     */
    public <T extends SensorData> SensorApi<T> getSensor(final String commercialName, final Class<T> sensorDataClass, final String sensorAlias) {
        return new SensorApiImpl<>(systemCore.getFileSystem(), commercialName, sensorDataClass, sensorAlias);
    }

    public <T extends SensorData> SensorApi<T> getSensor(final String commercialName, final Class<T> sensorDataClass) {
        return getSensor(commercialName, sensorDataClass, null);
    }
    
    public List<String> getCommercialNames() {
        final List<String> l = new ArrayList<>();
        l.addAll(systemCore.getFileSystem().getChildren(FileSystemEntries.SENSORS.buildPath()));
        return l;
    }
}
