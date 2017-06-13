
package il.ac.technion.cs.smarthouse.system.services.sensors_service;

/**
 * A base class for all sensor classes that will be used by the
 * SmartHouseApplication
 * 
 * @author RON
 * @since 28-12-2016
 */
public class SensorData {
    protected String commercialName;
    protected String sensorLocation;
    protected String sensorAlias;

    public SensorData() {}

    public String getCommercialName() {
        return commercialName;
    }

    public String getSensorLocation() {
        return sensorLocation;
    }
    
    public String getSensorAlias() {
        return sensorAlias;
    }
}
