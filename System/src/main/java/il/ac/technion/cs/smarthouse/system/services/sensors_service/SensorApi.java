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
    String subscribe(Consumer<T> functionToRun);

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
    String subscribeOnTime(Consumer<T> functionToRun, LocalTime timeToStartOn);
    
    String subscribeOnTime(Consumer<T> functionToRun, LocalTime timeToStartOn, long miliseconds);
    
    String subscribeOnTime(Consumer<T> functionToRun, long miliseconds);
    
    /**
     * Runs the given function when the sensor is found. If the sensor was already found, the function runs immediately
     * @param functionToRun
     * @return
     */
    String runWhenSensorIsFound(Consumer<T> functionToRun);
    
    void unsubscribe(String listenerId);

    /**
     * Send a message to a sensor.
     * 
     * @param instruction
     *            the message that the sensor will receive
     */
    void instruct(String instruction, String... path);
}
