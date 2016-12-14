/**
 * 
 */
package il.ac.technion.cs.eldery.system;

import java.util.Optional;
import java.util.function.Consumer;

import il.ac.technion.cs.eldery.utils.Tuple;

/**
 * @author Elia
 * @since Dec 13, 2016
 */
public interface DatabaseHandlerAPI {
    /** Adds a listener to a certain sensor, to be called on any update from that sensor
     *  @param sensorCommercialName The name of sensor, agreed upon in an external platform
     *  @param notifee The consumer to be called on a change, with the new data
     *  @return True if the registration was successful, false otherwise
     * */
    <L,R> Boolean addListener(String sensorCommercialName, Consumer<Tuple<L,R>> notifee);
    
    /**Queries the info of a sensor. 
     * @param sensorCommercialName The name of sensor, agreed upon in an external platform
     * @return the most updated data of the sensor, or Optional.empty() if the request couldn't be completed
     * */
    <L,R> Optional<Tuple<L,R>> getLastEntryOf(final String sensorCommercialName);
}
