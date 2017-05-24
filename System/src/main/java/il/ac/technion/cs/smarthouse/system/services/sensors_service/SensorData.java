package il.ac.technion.cs.smarthouse.system.services.sensors_service;

import il.ac.technion.cs.smarthouse.system.SensorLocation;

/**
 * A base class for all sensor classes that will be used by the
 * SmartHouseApplication
 * 
 * @author RON
 * @author Inbal Zukerman
 * @since 28-12-2016
 */
public class SensorData {

    protected SensorLocation sensorLocation;

    public SensorData() {}

    public SensorLocation getSensorLocation() {
        return sensorLocation;
    }
}
