/**
 * @author Elia
 * @since Dec 13, 2016
 */

package il.ac.technion.cs.eldery.system.applications;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import il.ac.technion.cs.eldery.system.AppThread;
import il.ac.technion.cs.eldery.system.DatabaseHandlerAPI;
import il.ac.technion.cs.eldery.system.EmergencyLevel;
import il.ac.technion.cs.eldery.system.exceptions.ApplicationNotRegisteredToEvent;
import il.ac.technion.cs.eldery.utils.Tuple;


/** API allowing smart house applications to register for information and notify on emergencies
 * */
public class ApplicationHandler {
    Map<String, Tuple<ApplicationManager, AppThread>> apps = new HashMap<>();//TODO: change to ApplicationIdentifier only, when wanted behavior is implemented
    DatabaseHandlerAPI databaseHandler;
    
    /**
     * Initialize the applicationHandler with the database responsible of managing the data in the current session
     */
    public ApplicationHandler(final DatabaseHandlerAPI databaseHandler) {
        this.databaseHandler = databaseHandler;
    }
    
    /** Adds a new application to the system.
     *  @return The id of the application in the system
     * */
    public String addApplication(final ApplicationManager appid, final SmartHouseApplication app){
        //TODO: ELIA remove the app param, after ApplicationIdentifier is completed
        apps.put(appid.getId(), new Tuple<>(appid, new AppThread(app)));
        return appid.getId();
    }
    
    /** Allows registration to a sensor. on update, the data will be given to the consumer for farther processing
     * @param id The id given to the app when added to the system
     * @param sensorCommercialName The name of sensor, agreed upon in an external platform
     * @param notifyWhen A predicate that will be called every time the sensor updates the date. If it returns true the consumer will be called
     * @param notifee A consumer that will receive the new data from the sensor
     * @return True if the registration was successful, false otherwise
     * */
    public <L,R> Boolean registerToSensor(final String id, final String sensorCommercialName, 
            final Predicate<Tuple<L,R>> notifyWhen, final Consumer<Tuple<L,R>> notifee){
        try{
            AppThread app = apps.get(id).getRight();
            final Long eventId = app.registerEventConsumer(notifee);
            final Consumer<Tuple<L,R>> $ = t -> {
                  if(notifyWhen.test(t))
                      try {
                          app.notifyOnEvent(eventId, t);
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
     * @param id The id given to the app when added to the system
     * @param sensorCommercialName The name of sensor, agreed upon in an external platform
     * @param t the time when a polling is requested
     * @param notifee A consumer that will receive the new data from the sensor
     * @return True if the registration was successful, false otherwise
     * */
    public <L,R> Boolean registerToSensor(final String id,final String sensorCommercialName, final LocalTime t, 
            final Consumer<Tuple<L,R>> notifee){
        return Boolean.FALSE; //TODO: ELIA implement
    }
    
    /** Request for the latest data received by a sensor
     *  @param sensorCommercialName The name of sensor, agreed upon in an external platform
     *  @return the latest data (or Optional.empty() if the query failed in any point)
     * */
    public <L,R> Optional<Tuple<L,R>> querySensor(final String sensorCommercialName){
        return databaseHandler.getLastEntryOf(sensorCommercialName);
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
