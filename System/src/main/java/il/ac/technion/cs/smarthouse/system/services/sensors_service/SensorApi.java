package il.ac.technion.cs.smarthouse.system.services.sensors_service;

import java.time.LocalTime;
import java.util.function.Consumer;

import il.ac.technion.cs.smarthouse.system.SensorLocation;

public interface SensorApi<T extends SensorData> {
    String getCommercialName();
    
    boolean isConnected();
    
    /**
     * Queries the system for the sensor's current location
     * 
     * @return the sensors location
     */
    SensorLocation getSensorLocation();
    
    /**
     * Allows registration to a sensor. on update, the data will be given to the
     * consumer for farther processing
     * 
     * @param functionToRun
     *            A consumer that will receive a seneorClass object initialized
     *            with the newest data from the sensor
     * @throws SensorLostRuntimeException
     */
    String subscribe(final Consumer<T> functionToRun);

    /**
     * Allows registration to a sensor. on time, the sensor will be polled and
     * 
     * @param t
     *            the time when a polling is requested
     * @param functionToRun
     *            A consumer that will receive a seneorClass object initialized
     *            with the newest data from the sensor
     * @param repeat
     *            <code>false</code> if you want to query the sensor on the
     *            given time only once, <code>true</code> otherwise (query at
     *            this time FOREVER)
     * @return 
     */
    String subscribeOnTime(final LocalTime t, final Consumer<T> functionToRun, final Boolean repeat);
    
    void unsubscribe(String listenerId);

    /**
     * Send a message to a sensor.
     * 
     * @param instruction
     *            the message that the sensor will receive
     */
    void instruct(final String instruction, final String... path);
}
