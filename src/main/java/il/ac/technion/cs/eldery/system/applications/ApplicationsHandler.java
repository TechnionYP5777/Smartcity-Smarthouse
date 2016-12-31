package il.ac.technion.cs.eldery.system.applications;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import com.google.gson.Gson;

import il.ac.technion.cs.eldery.system.DatabaseHandler;
import il.ac.technion.cs.eldery.system.EmergencyLevel;
import il.ac.technion.cs.eldery.system.applications.api.SensorData;
import il.ac.technion.cs.eldery.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.eldery.system.applications.api.exceptions.OnLoadException;
import il.ac.technion.cs.eldery.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.eldery.system.exceptions.AppInstallerException;
import il.ac.technion.cs.eldery.system.exceptions.ApplicationInitializationException;
import il.ac.technion.cs.eldery.system.exceptions.SensorNotFoundException;

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
    static <T extends SensorData> Consumer<String> generateSensorListener(final Class<T> sensorClass, final Consumer<T> functionToRun) {
        return jsonData -> {
            functionToRun.accept(new Gson().fromJson(jsonData, sensorClass));
        };
    }

    static Date localTimeToDate(final LocalTime ¢) {
        return Date.from(¢.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
    }

    // ----------- public methods -------------------

    /** Initialize the applicationHandler with the database responsible of
     * managing the data in the current session */
    public ApplicationsHandler(final DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    /** Adds a new application to the system, and presents it to the screen
     * @throws IOException
     * @throws AppInstallerException
     * @throws OnLoadException
     * @throws ApplicationInitializationException */
    public void addApplication(final String appId, final ApplicationPath<?> appPath) throws AppInstallerException, IOException, OnLoadException {
        // TODO: Elia - maybe we should init the appId in here...
        final ApplicationManager $ = new ApplicationManager(appId, appPath, this);
        $.initialize();
        // $.minimize();
        apps.put(appId, $);
    }

    /** See
     * {@link SmartHouseApplication#subscribeToSensor(String, Class, Consumer)} */
    public final <T extends SensorData> void subscribeToSensor(final String sensorId, final Class<T> sensorClass, final Consumer<T> functionToRun)
            throws SensorNotFoundException {
        databaseHandler.addListener(sensorId, generateSensorListener(sensorClass, functionToRun));
    }

    /** See
     * {@link SmartHouseApplication#subscribeToSensor(String, LocalTime, Class, Consumer, Boolean)} */
    public final <T extends SensorData> void subscribeToSensor(final String sensorId, final LocalTime t, final Class<T> sensorClass,
            final Consumer<T> functionToRun, final Boolean repeat) {
        new Timer().schedule(new QueryTimerTask(sensorId, t, generateSensorListener(sensorClass, functionToRun), repeat), localTimeToDate(t));
    }

    /** See
     * {@link SmartHouseApplication#receiveLastEntries(String, Class, int)} */
    public final <T extends SensorData> List<T> querySensor(final String sensorId, final Class<T> sensorClass, final int numOfEntries)
            throws SensorNotFoundException {
        final List<T> $ = new LinkedList<>();
        final List<String> $_raw = databaseHandler.getList(sensorId);
        final Consumer<String> adder = generateSensorListener(sensorClass, x -> $.add(x));
        for (int ¢ = 0; ¢ < numOfEntries && ¢ < $_raw.size(); ++¢)// TODO: ELIA
                                                                  // varify
                                                                  // assumption
                                                                  // - newest
                                                                  // entry at 0
            adder.accept($_raw.get(¢));
        return $;
    }

    /** See {@link SmartHouseApplication#sendAlert(String, EmergencyLevel)} */
    public void alertOnAbnormalState(final String message, final EmergencyLevel eLevel) {
        // TODO: ELIA implement
    }
}
