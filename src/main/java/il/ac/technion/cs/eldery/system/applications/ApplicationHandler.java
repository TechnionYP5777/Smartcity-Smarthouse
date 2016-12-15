package il.ac.technion.cs.eldery.system.applications;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.function.Predicate;

import il.ac.technion.cs.eldery.system.AppThread;
import il.ac.technion.cs.eldery.system.DatabaseHandlerAPI;
import il.ac.technion.cs.eldery.system.EmergencyLevel;
import il.ac.technion.cs.eldery.system.exceptions.ApplicationNotRegisteredToEvent;
import il.ac.technion.cs.eldery.utils.Generator;
import il.ac.technion.cs.eldery.utils.Table;
import il.ac.technion.cs.eldery.utils.Tuple;


/** API allowing smart house applications to register for information and notify on emergencies
 * 
 * @author Elia
 * @since Dec 13, 2016
 * */
public class ApplicationHandler {
    private class QueryTimerTask extends TimerTask{
        Boolean repeat;
        String sensorCommercialName;
        LocalTime t;
        Consumer<Table> notifee;
        
        QueryTimerTask(final String sensorCommercialName, final LocalTime t, final Consumer<Table> notifee, 
                Boolean repeat){
            this.repeat = repeat;
            this.notifee = notifee;
            this.t = t;
            this.sensorCommercialName = sensorCommercialName;
        }
        
        void setRepeat(Boolean b){
            repeat = b;
        }
        
        /* (non-Javadoc)
         * @see java.util.TimerTask#run()
         */
        @SuppressWarnings("boxing")
        @Override public void run() {
            notifee.accept(querySensor(sensorCommercialName).orElse(new Table()));
            if(repeat)
                new Timer().schedule(this, localTimeToDate(t));
        }
        
    }
    
    Map<String, Tuple<ApplicationManager, AppThread>> apps = new HashMap<>();//TODO: change to ApplicationManager only, when wanted behavior is implemented
    Map<String, QueryTimerTask> timedQuerires = new HashMap<>();
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
     * @param id The id given to the application when added to the system
     * @param sensorCommercialName The name of sensor, agreed upon in an external platform
     * @param notifyWhen A predicate that will be called every time the sensor updates the date. If it returns true the consumer will be called
     * @param notifee A consumer that will receive the new data from the sensor
     * @param numOfEntries The number of entries the application want to receive from the sensor upon update 
     * @return The registration id if the action was successful, otherwise <code>null</code>
     * */
    public String registerToSensor(final String id, final String sensorCommercialName, final Predicate<Table> notifyWhen, 
            final Consumer<Table> notifee, int numOfEntries){
        try{
            AppThread app = apps.get(id).getRight();
            final String eventId = app.registerEventConsumer(notifee);
            return databaseHandler.addListener(sensorCommercialName, t -> {
                if (notifyWhen.test(t))
                    try {
                        app.notifyOnEvent(eventId, t.receiveKLastEntries(numOfEntries));
                    } catch (final ApplicationNotRegisteredToEvent e) {
                        e.printStackTrace();
                    }
            });
        }catch(@SuppressWarnings("unused") final Exception __){
            return null;
        }
    }
    
    static Date localTimeToDate(final LocalTime ¢){
        return Date.from(¢.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
    }
    
    /** Allows registration to a sensor. on time, the sensor will be polled and the data will be given to the consumer for 
     * farther processing 
     * @param sensorCommercialName The name of sensor, agreed upon in an external platform
     * @param t the time when a polling is requested
     * @param notifee A consumer that will receive the new data from the sensor, or an empty table if the was no new information.
     * @param repeat <code>false</code> if you want to query the sensor only once, <code>true</code> otherwise (query at this time FOREVER)
     * @return The registration id if the action was successful, otherwise <code>null</code>
     * */
    public String registerToSensor(final String sensorCommercialName, final LocalTime t, final Consumer<Table> notifee, 
            Boolean repeat){
        QueryTimerTask task = new QueryTimerTask(sensorCommercialName, t, notifee, repeat);
        String $ = Generator.GenerateUniqueIDstring();
        timedQuerires.put($, task);
        new Timer().schedule(task, localTimeToDate(t));
        return $;
    }
    
    /** Removes an existing registration. The consumer given at registration will not be called again.
     *  @param id The id given at registration
     * */
    public void cancelRegistration(final String id){
        if (timedQuerires.get(id) == null)
            databaseHandler.removeListener(id);
        else
            timedQuerires.get(id).setRepeat(Boolean.FALSE);
    }
    
    /** Request for the latest data received by a sensor
     *  @param sensorCommercialName The name of sensor, agreed upon in an external platform
     *  @return the latest data (or Optional.empty() if the query failed in any point)
     * */
    public Optional<Table> querySensor(final String sensorCommercialName){
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
