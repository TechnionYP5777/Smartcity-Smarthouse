 package il.ac.technion.cs.eldery.system.applications;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.codehaus.jackson.map.ObjectMapper;

import il.ac.technion.cs.eldery.system.AppThread;
import il.ac.technion.cs.eldery.system.DatabaseHandler;
import il.ac.technion.cs.eldery.system.EmergencyLevel;
import il.ac.technion.cs.eldery.system.applications.api.SensorData;
import il.ac.technion.cs.eldery.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.eldery.system.exceptions.ApplicationInitializationException;
import il.ac.technion.cs.eldery.system.exceptions.SensorNotFoundException;
import il.ac.technion.cs.eldery.utils.Generator;
import il.ac.technion.cs.eldery.utils.Table;
import il.ac.technion.cs.eldery.utils.Tuple;

/** API allowing smart house applications to register for information and notify
 * on emergencies
 * @author Elia Traore
 * @author Inbal Zukerman
 * @since Dec 13, 2016 */
public class ApplicationsHandler {
    private class QueryTimerTask extends TimerTask {
        Boolean repeat;
        String sensorId;
        LocalTime t;
        Consumer<String> notifee;

        QueryTimerTask(final String sensorId, final LocalTime t, final Consumer<String> notifee, final Boolean repeat) {
            this.repeat = repeat;
            this.notifee = notifee;
            this.t = t;
            this.sensorId = sensorId;
        }

        /* (non-Javadoc)
         * 
         * @see java.util.TimerTask#run() */
        @Override @SuppressWarnings({ "boxing" }) public void run() {
            notifee.accept(databaseHandler.getLastEntryOf(sensorId).orElse(new String()));
            if (repeat)
                new Timer().schedule(this, localTimeToDate(t));
        }

    }

    Map<String, ApplicationManager> apps = new HashMap<>();
    DatabaseHandler databaseHandler;
    
    // ----------- not-public methods ---------------   
    static <T extends SensorData> Consumer<String> generateSensorListener( final Class<T> sensorClass, 
            final Consumer<T> functionToRun){
        return jsonData ->{T data = null;
                              try {
                                  data = new ObjectMapper().readValue(jsonData, sensorClass);
                              } catch (IOException e) {
                                  // TODO: Auto-generated catch block 
                                  //TODO: RON - what is the desired behavior in this case?
                                  e.printStackTrace();
                              }
                              functionToRun.accept(data);
                           };
    }
    
    static Date localTimeToDate(final LocalTime ¢) {
        return Date.from(¢.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
    }
    
    
    // ----------- public methods -------------------
    
    /** Initialize the applicationHandler with the database responsible of managing the data in the current session */
    public ApplicationsHandler(final DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }
    
    public DatabaseHandler getDatabaseHandler(){
        return databaseHandler;
    }
    
    /** Adds a new application to the system, and presents it to the screen
     * @throws ApplicationInitializationException 
     */
    public void addApplication(final String appId, final String jarPath) throws ApplicationInitializationException {
        ApplicationManager $ = new ApplicationManager(appId, jarPath, this);
        if(!$.initialize()){
            throw new ApplicationInitializationException();
        }
        $.initialize();
//        $.minimize();
        apps.put(appId, $);
    }

    /** See {@link SmartHouseApplication#subscribeToSensor(String, Class, Consumer)}
     * */
    public final <T extends SensorData> void subscribeToSensor(final String sensorId, final Class<T> sensorClass, 
            final Consumer<T> functionToRun) throws SensorNotFoundException{
        databaseHandler.addListener(sensorId, generateSensorListener(sensorClass, functionToRun));
    }

    /** See {@link SmartHouseApplication#subscribeToSensor(String, LocalTime, Class, Consumer, Boolean)}
     * */
    public final <T extends SensorData> void subscribeToSensor(final String sensorId, final LocalTime t, final Class<T> sensorClass, 
            final Consumer<T> functionToRun, final Boolean repeat) {
        final QueryTimerTask task = new QueryTimerTask(sensorId, t, generateSensorListener(sensorClass, functionToRun), repeat);
        new Timer().schedule(task, localTimeToDate(t));
    }


    /** Request for the latest data received by a sensor
     * @param sensorId The ID of the sensor, returned from
     *        inquireAbout(sensorCommercialName)
     * @return the latest data (or Optional.empty() if the query failed in any
     *         point) */
    public Optional<String> querySensor(final String sensorId) {
        return databaseHandler.getLastEntryOf(sensorId);
    }

    /** Report an abnormality in the expected schedule. The system will contact
     * the needed personal, according to the urgency level
     * @param message Specify the abnormality, will be presented to the
     *        contacted personal
     * @param eLevel The level of personnel needed in the situation */
    public void alertOnAbnormalState(final String message, final EmergencyLevel eLevel) {
        // TODO: ELIA implement
    }
}
