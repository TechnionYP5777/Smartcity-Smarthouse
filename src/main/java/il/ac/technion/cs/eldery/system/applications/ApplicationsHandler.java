package il.ac.technion.cs.eldery.system.applications;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import il.ac.technion.cs.eldery.system.DatabaseHandler;
import il.ac.technion.cs.eldery.system.EmergencyLevel;
import il.ac.technion.cs.eldery.system.SystemCore;
import il.ac.technion.cs.eldery.system.applications.api.SensorData;
import il.ac.technion.cs.eldery.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.eldery.system.applications.api.exceptions.OnLoadException;
import il.ac.technion.cs.eldery.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.eldery.system.exceptions.AppInstallerException;
import il.ac.technion.cs.eldery.system.exceptions.ApplicationInitializationException;
import il.ac.technion.cs.eldery.system.exceptions.SensorNotFoundException;
import il.ac.technion.cs.eldery.utils.Generator;

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
        @Override @SuppressWarnings("boxing") public void run() {
            notifee.accept(databaseHandler.getLastEntryOf(sensorId).orElse(new String()));
            if (repeat)
                new Timer().schedule(this, localTimeToDate(t));
        }

    }

    List<ApplicationManager> apps = new ArrayList<>();
    DatabaseHandler databaseHandler;
    SystemCore systemCore;

    // ----------- not-public methods ---------------
    static <T extends SensorData> Consumer<String> generateSensorListener(final Class<T> sensorClass, final Consumer<T> functionToRun) {
        return jsonData -> functionToRun.accept(new Gson().fromJson(jsonData, sensorClass));
    }

    static Date localTimeToDate(final LocalTime ¢) {
        return Date.from(¢.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
    }

    // ----------- public methods -------------------

    /** Initialize the applicationHandler with the database responsible of
     * managing the data in the current session */
    public ApplicationsHandler(final DatabaseHandler databaseHandler, final SystemCore systemCore) {
        this.databaseHandler = databaseHandler;
        this.systemCore = systemCore;
    }

    // [start] Services to the SystemCore
    /** Adds a new application to the system, and initializes it
     * @throws IOException
     * @throws AppInstallerException
     * @throws OnLoadException
     * @throws ApplicationInitializationException */
    public ApplicationManager addApplication(final ApplicationPath<?> appPath) throws AppInstallerException, IOException, OnLoadException {
        final ApplicationManager $ = new ApplicationManager(Generator.GenerateUniqueIDstring(), appPath, this);
        $.initialize();
        apps.add($);
        return $;
    }

    public List<ApplicationManager> getApplicationManagers() {
        return Collections.unmodifiableList(apps);
    }
    // [end]

    // [start] Services to SmartHouseApplications - These services will be
    // wrapped by the SmartHouseApplication API
    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
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
        return databaseHandler.getList(sensorId).getLastKEntries(numOfEntries).stream().map(jsonData -> new Gson().fromJson(jsonData, sensorClass))
                .collect(Collectors.toList());
    }

    /** See {@link SmartHouseApplication#sendAlert(String, EmergencyLevel)} */
    public void alertOnAbnormalState(final String message, final EmergencyLevel eLevel) {
        systemCore.alert(message, eLevel);
    }
    // [end]
}
