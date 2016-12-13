/**
 * 
 */
package il.ac.technion.cs.eldery.system.handlers;

import java.util.Optional;
import java.util.function.Consumer;

import il.ac.technion.cs.eldery.utils.Tuple;

/**
 * @author Elia
 * @since Dec 13, 2016
 */
public interface DatabaseHandlerAPI {
    <L,R> Boolean addListener(String sensorCommercialName, Consumer<Tuple<L,R>> notifee);
    <L,R> Optional<Tuple<L,R>> getLastEntryOf(final String sensorCommercialName);
}
