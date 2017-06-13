package il.ac.technion.cs.smarthouse.system.services.sensors_service;

import java.time.LocalTime;
import java.util.List;
import java.util.function.Consumer;

/**
 * An API class for the developers, that allows interactions with a specific
 * sensor.
 * 
 * @author RON
 * @since 29-05-2017
 * @param <T>
 *            the sensor's messages will be deserialize into this class. T must
 *            extend SensorData
 */
public interface SensorApi<T extends SensorData> {
    /**
     * Get the sensor's commercial name
     * 
     * @return the sensor's commercial name
     */
    String getCommercialName();

    /**
     * Queries the system for the sensor's current alias
     * 
     * @return the sensors alias.<br>
     *         if the sensor was not found yet, "" will be returned
     */
    String getSensorAlias();
    
    List<String> getAllAliases();
    
    String listenForNewAliases(Consumer<String> functionToRun);

    /**
     * Check if the sensor was already found by the SensorApi
     * 
     * @return true if the sensor was found, false if not found yet
     */
    boolean isConnected();

    /**
     * Queries the system for the sensor's current location
     * 
     * @return the sensors location.<br>
     *         if the sensor was not found yet, "" will be returned
     */
    String getSensorLocation();

    /**
     * Allows registration to a sensor. On an update from the sensor, the data
     * will be given to the consumer for farther processing
     * 
     * @param functionToRun
     *            A consumer that will receive a {@link T} object initialized
     *            with the newest data from the sensor
     * @return the ID of the subscribed function. Use this ID to unsubscribe the
     *         listener with {@link SensorApi#unsubscribe(String)}
     */
    String subscribe(Consumer<T> functionToRun);

    /**
     * Subscribe to a sensor on time.<br>
     * On time, the sensor will be polled and functionToRun will be executed
     * with the current sensors data
     * <p>
     * <code>functionToRun</code> will be executed once at
     * <code>timeToStartOn</code>
     * 
     * @param functionToRun
     *            A consumer that will receive a {@link T} object initialized
     *            with the newest data from the sensor
     * @param timeToStartOn
     *            The time at which task is to be executed. If the time is in
     *            the past, the task is scheduled for immediate execution
     * @return the ID of the subscribed function. Use this ID to unsubscribe the
     *         listener with {@link SensorApi#unsubscribe(String)}
     */
    String subscribeOnTime(Consumer<T> functionToRun, LocalTime timeToStartOn);

    /**
     * Subscribe to a sensor on time.<br>
     * On time, the sensor will be polled and functionToRun will be executed
     * with the current sensors data
     * <p>
     * <code>functionToRun</code> will be executed at
     * <code>timeToStartOn</code>, and repeat executing every
     * <code>milliseconds</code> milliseconds
     * 
     * @param functionToRun
     *            A consumer that will receive a {@link T} object initialized
     *            with the newest data from the sensor
     * @param timeToStartOn
     *            The time at which task is to be executed. If the time is in
     *            the past, the task is scheduled for immediate execution
     * @param milliseconds
     *            The time in milliseconds between successive executions of
     *            functionToRun
     * @return the ID of the subscribed function. Use this ID to unsubscribe the
     *         listener with {@link SensorApi#unsubscribe(String)}
     */
    String subscribeOnTime(Consumer<T> functionToRun, LocalTime timeToStartOn, long milliseconds);

    /**
     * Subscribe to a sensor on time.<br>
     * On time, the sensor will be polled and functionToRun will be executed
     * with the current sensors data
     * <p>
     * <code>functionToRun</code> will be executed every
     * <code>milliseconds</code> milliseconds
     * 
     * @param functionToRun
     *            A consumer that will receive a {@link T} object initialized
     *            with the newest data from the sensor
     * @param milliseconds
     *            The time in milliseconds between successive executions of
     *            functionToRun
     * @return The ID of the subscribed function. Use this ID to unsubscribe the
     *         listener with {@link SensorApi#unsubscribe(String)}
     */
    String subscribeOnTime(Consumer<T> functionToRun, long milliseconds);

    /**
     * Runs the given function when the sensor is found. If the sensor was
     * already found, the function runs immediately.
     * 
     * @param functionToRun
     *            A consumer that will receive a {@link T} object initialized
     *            with the newest data from the sensor
     * @return The ID of the subscribed function. Use this ID to unsubscribe the
     *         listener with {@link SensorApi#unsubscribe(String)}
     */
    String runWhenSensorIsFound(Consumer<T> functionToRun);

    /**
     * Restart the sensor API with a new sensor alias. <br>
     * Note that all of the listeners will not be deleted, and will be
     * re-enabled when a new sensor is found
     * 
     * @param alias
     */
    void reselectSensorByAlias(String alias);

    /**
     * Unsubscribe a subscribed listener. If the ID isn't valid, nothing will
     * happen.
     * 
     * @param listenerId
     *            The ID of the subscribed function
     */
    void unsubscribe(String listenerId);

    /**
     * Send a message to a sensor.<br>
     * If the sensors is not yet found, the instructions will be buffered and
     * they will be passed to the sensor when it is found.
     * 
     * @param instruction
     *            The message that the sensor will receive
     * @param path
     *            The path to send the instruction on
     */
    void instruct(String instruction, String... path);
}
