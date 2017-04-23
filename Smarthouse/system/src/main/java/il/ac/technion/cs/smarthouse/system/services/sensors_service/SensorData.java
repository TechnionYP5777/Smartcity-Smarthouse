package il.ac.technion.cs.smarthouse.system.services.sensors_service;

import il.ac.technion.cs.smarthouse.system.SensorLocation;

/** A base class for all sensor classes that will be used by the
 * SmartHouseApplication
 * @author RON
 * @since 28-12-2016 */
public class SensorData {
    protected String commercialName;
    protected SensorLocation sensorLocation;

    public SensorData() {}

    public String getCommercialName() {
        return commercialName;
    }

    public SensorLocation getSensorLocation() {
        return sensorLocation;
    }
}
