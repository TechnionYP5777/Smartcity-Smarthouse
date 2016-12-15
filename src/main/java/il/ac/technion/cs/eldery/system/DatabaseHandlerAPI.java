/**
 * 
 */
package il.ac.technion.cs.eldery.system;

import java.util.Optional;
import java.util.function.Consumer;

import il.ac.technion.cs.eldery.utils.Table;

/**
 * The API required by ApplicationHandler in order to allow it desired functionalities.
 * @author Elia
 * @since Dec 13, 2016
 */
public interface DatabaseHandlerAPI {
    /** Adds a listener to a certain sensor, to be called on <strong>any</strong> update from that sensor
     *  @param sensorCommercialName The name of sensor, agreed upon in an external platform
     *  @param notifee The consumer to be called on a change, with the new data
     *  @return The id of the listener, to be used in any future refernce to it
     * */
    String addListener(String sensorCommercialName, Consumer<Table> notifee);
    
    /** Remove a previously added listener
     * @param listenerId The id given when the listener was added to the system
     * */
    void removeListener(String listenerId);
    
    /**Queries the info of a sensor. 
     * @param sensorCommercialName The name of sensor, agreed upon in an external platform
     * @return the most updated data of the sensor, or Optional.empty() if the request couldn't be completed for any reason
     * */
    Optional<Table> getLastEntryOf(final String sensorCommercialName);
}
