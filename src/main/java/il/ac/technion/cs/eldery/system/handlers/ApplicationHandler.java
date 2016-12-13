/**
 * @author Elia
 * @since Dec 13, 2016
 */

package il.ac.technion.cs.eldery.system.handlers;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import il.ac.technion.cs.eldery.system.AppThread;
import il.ac.technion.cs.eldery.system.EmergencyLevel;
import il.ac.technion.cs.eldery.system.applications.ApplicationIdentifier;
import il.ac.technion.cs.eldery.system.exceptions.ApplicationNotRegisteredToEvent;
import il.ac.technion.cs.eldery.utils.Tuple;


/** API allowing smart house applications to register for information and notify on emergencies
 * */
public class ApplicationHandler {
    Map<ApplicationIdentifier, AppThread> apps = new HashMap<>();
    DatabaseHandlerAPI databaseHandler;
    
    /**
     * Initialize the applicationHandler with the database responsible of managing the data in the current session
     */
    public ApplicationHandler(final DatabaseHandlerAPI databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    
    /** Allows registration to a sensor. on update, the data will be given to the consumer for farther processing
     * @param id The identification of the application requesting to register
     * @param sensorCommercialName The name of sensor, agreed upon in an external platform
     * @param notifyWhen A predicate that will be called every time the sensor updates the date. If it returns true the consumer will be called
     * @param notifee A consumer that will receive the new data from the sensor
     * @return True if the registration was successful, false otherwise
     * */
    public <L,R> Boolean registerToSensor(final ApplicationIdentifier id, final String sensorCommercialName, 
            final Predicate<Tuple<L,R>> notifyWhen, final Consumer<Tuple<L,R>> notifee){
        try{
            final Long eventId = apps.get(id).registerEventConsumer(notifee);
            final Consumer<Tuple<L,R>> $ = t -> {
                  if(notifyWhen.test(t))
                      try {
                          apps.get(id).notifyOnEvent(eventId, t);
                      } catch (final ApplicationNotRegisteredToEvent e) {
                          e.printStackTrace();
                      }                
              };
            return databaseHandler.addListener(sensorCommercialName, $);
        }catch(@SuppressWarnings("unused") final Exception __){
            return Boolean.FALSE;
        }
    }
    
    /** Allows registration to a sensor. on time, the sensor will be polled and the data will be given to the consumer for 
     * farther processing 
     * @param id The identification of the application requesting to register
     * @param sensorCommercialName The name of sensor, agreed upon in an external platform
     * @param t the time when a polling is requested
     * @param notifee A consumer that will receive the new data from the sensor
     * @return True if the registration was successful, false otherwise
     * */
    public <L,R> Boolean registerToSensor(final ApplicationIdentifier id,final String sensorCommercialName, final LocalTime t, 
            final Consumer<Tuple<L,R>> notifee){
        return Boolean.FALSE; //TODO: ELIA implement
    }
    
    /** Request for the latest data received by a sensor
     *  @param sensorCommercialName The name of sensor, agreed upon in an external platform
     *  @return the latest data (or Optional.empty() if the query failed in any point)
     * */
    public <L,R> Optional<Tuple<L,R>> querySensor(final String sensorCommercialName){
        return databaseHandler.querySensor(sensorCommercialName);
    }
    
    /** Report an abnormality in the expected schedule. The system will contact the needed personal, according to the 
     *  urgency level
     *  @param message Specify the abnormality, will be presented to the contacted personal
     *  @param eLevel The level of personnel needed in the situation
     * */
    public void alertOnAbnormalState(final String message, final EmergencyLevel eLevel){
        //TODO: ELIA implement
    }
}
