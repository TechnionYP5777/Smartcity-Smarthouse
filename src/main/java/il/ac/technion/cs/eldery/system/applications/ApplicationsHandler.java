 package il.ac.technion.cs.eldery.system.applications;

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

import il.ac.technion.cs.eldery.system.AppThread;
import il.ac.technion.cs.eldery.system.DatabaseHandler;
import il.ac.technion.cs.eldery.system.EmergencyLevel;
import il.ac.technion.cs.eldery.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.eldery.system.exceptions.FailedApplicationInitializationException;
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
        String sensorCommercialName;
        LocalTime t;
        Consumer<Table<String, String>> notifee;

        QueryTimerTask(final String sensorCommercialName, final LocalTime t, final Consumer<Table<String, String>> notifee, final Boolean repeat) {
            this.repeat = repeat;
            this.notifee = notifee;
            this.t = t;
            this.sensorCommercialName = sensorCommercialName;
        }

        void setRepeat(final Boolean ¢) {
            repeat = ¢;
        }

        /* (non-Javadoc)
         * 
         * @see java.util.TimerTask#run() */
        @Override @SuppressWarnings({ "boxing" }) public void run() {
            /* TODO: ELIA- this code should be changed according to the changes
             * in DatabaseHandler
             * notifee.accept(querySensor(sensorCommercialName).orElse(new
             * Table<String, String>())); */
            if (repeat)
                new Timer().schedule(this, localTimeToDate(t));
        }

    }

    Map<String, ApplicationManager> apps = new HashMap<>();
    DatabaseHandler databaseHandler;

    /** Initialize the applicationHandler with the database responsible of
     * managing the data in the current session */
    public ApplicationsHandler(final DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    /** Adds a new application to the system, and presents it to the screen
     * @throws FailedApplicationInitializationException 
     */
    public void addApplication(final String appId, final String jarPath) throws FailedApplicationInitializationException {
        ApplicationManager $ = new ApplicationManager(appId, jarPath, this);
        if(!$.initialize()){
            throw new FailedApplicationInitializationException();
        }
        $.initialize();
        $.minimize();
        apps.put(appId, $);
    }
    
    public DatabaseHandler getDatabaseHandler(){
        return databaseHandler;
    }

    /** Allows registration to a sensor. on update, the data will be given to
     * the consumer for farther processing
     * @param id The id given to the application when added to the system
     * @param sensorId The ID of the sensor, returned from
     *        inquireAbout(sensorCommercialName)
     * @param notifyWhen A predicate that will be called every time the sensor
     *        updates the date. If it returns true the consumer will be called
     * @param notifee A consumer that will receive the new data from the sensor
     * @param numOfEntries The number of entries the application want to receive
     *        from the sensor upon update
     * @return The registration id if the action was successful, otherwise
     *         <code>null</code> */
    public String registerToSensor(final String id, final String sensorId, final Predicate<Table<String, String>> notifyWhen,
            final Consumer<Table<String, String>> notifee, final int numOfEntries) {
        try {
//            final AppThread $ = apps.get(id).right;
            final String eventId = $.registerEventConsumer(notifee);
            /* TODO: ELIA- this code should be changed according to the changes
             * in DatabaseHandler return databaseHandler.addListener(sensorId, t
             * -> { if (notifyWhen.test(t)) try { $.notifyOnEvent(eventId,
             * t.receiveKLastEntries(numOfEntries)); } catch (final
             * ApplicationNotRegisteredToEvent ¢) { ¢.printStackTrace(); }
             * }); */
        } catch (@SuppressWarnings("unused") final Exception __) {
            return null;
        }
        return null; // Added temporary until bug is fixed
    }

    static Date localTimeToDate(final LocalTime ¢) {
        return Date.from(¢.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
    }

    /** Allows registration to a sensor. on time, the sensor will be polled and
     * the data will be given to the consumer for farther processing
     * @param sensorId The ID of the sensor, returned from
     *        inquireAbout(sensorCommercialName)
     * @param t the time when a polling is requested
     * @param notifee A consumer that will receive the new data from the sensor,
     *        or an empty table if the was no new information.
     * @param repeat <code>false</code> if you want to query the sensor only
     *        once, <code>true</code> otherwise (query at this time FOREVER)
     * @return The registration id if the action was successful, otherwise
     *         <code>null</code> */
    public String registerToSensor(final String sensorId, final LocalTime t, final Consumer<Table<String, String>> notifee, final Boolean repeat) {
        final QueryTimerTask task = new QueryTimerTask(sensorId, t, notifee, repeat);
        final String $ = Generator.GenerateUniqueIDstring();
        new Timer().schedule(task, localTimeToDate(t));
        return $;
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
