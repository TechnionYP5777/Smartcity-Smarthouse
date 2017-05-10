package il.ac.technion.cs.smarthouse.system.services.sensors_service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import il.ac.technion.cs.smarthouse.networking.messages.UpdateMessage;
import il.ac.technion.cs.smarthouse.system.SensorLocation;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.exceptions.SensorNotFoundException;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;

/** An API class for the developers, that allows interactions with a specific
 * sensor.
 * @author RON
 * @since 07-04-2017
 * @param <T> the sensor's messages will be deserialize into this class. T must
 *        extend SensorData */
public final class SensorApi<T extends SensorData> {
    private static Logger log = LoggerFactory.getLogger(SensorApi.class);
    private static String LOG_MSG_RUNTIME_THROW = "SensorLostRuntimeException is being thrown. This is unexpected!";
    private static String LOG_MSG_SENSOR_NOT_FOUND = LOG_MSG_RUNTIME_THROW + " This is thrown because SensorNotFoundException was received";

    private SystemCore systemCore;
    private String sensorId;
    private Class<T> sensorDataClass;

    /** This c'tor should be used only by the {@link SensorsManager}
     * @param systemCore a reference to the system's core
     * @param sensorId The ID of the sensor, returned from
     *        inquireAbout(sensorCommercialName)
     * @param sensorDataClass The class representing the sensor being listened
     *        to, defined by the developer */
    SensorApi(final SystemCore systemCore, final String sensorId, final Class<T> sensorDataClass) {
        this.systemCore = systemCore;
        this.sensorId = sensorId;
        this.sensorDataClass = sensorDataClass;
    }

    /** @return the sensors commercial name */
    public String getCommercialName() {
        try {
            return systemCore.databaseHandler.getName(sensorId);
        } catch (SensorNotFoundException e) {
            log.error(LOG_MSG_SENSOR_NOT_FOUND, e);
            throw new SensorLostRuntimeException(e);
        }
    }

    /** Queries the system for the sensor's current location
     * @return the sensors location */
    public SensorLocation getSensorLocation() {
        try {
            return systemCore.databaseHandler.getSensorLocation(this.sensorId);
        } catch (SensorNotFoundException e) {
            log.error(LOG_MSG_SENSOR_NOT_FOUND, e);
            throw new SensorLostRuntimeException(e);
        }
    }

    /** Wraps the <code>functionToRun</code> Consumer with helpful wrappers.
     * <p>
     * 1. A wrapper that sets the <code>sensorData</code>'s
     * <code>commercialName</code> and <code>sensorLocation</code>
     * <p>
     * 2. Surrounds the given function with a Platform.runLater, if
     * <code>runOnFx == true</code>
     * @param functionToRun a Consumer that receives
     *        <code>SensorData sensorData</code> and operates on it.
     * @param runOnFx
     * @return the modified consumer [[SuppressWarningsSpartan]] */
    private Consumer<String> generateSensorListener(final Consumer<T> functionToRun, boolean runOnFx) {
        final Consumer<T> functionToRunWrapper1 = sensorData -> {
            sensorData.commercialName = getCommercialName();
            sensorData.sensorLocation = getSensorLocation();
            functionToRun.accept(sensorData);
        }, functionToRunWrapper2 = !runOnFx ? functionToRunWrapper1 : JavaFxHelper.surroundConsumerWithFx(functionToRunWrapper1);
        return jsonData -> functionToRunWrapper2.accept(new Gson().fromJson(jsonData, sensorDataClass));
    }

    /** Allows registration to a sensor. on update, the data will be given to
     * the consumer for farther processing
     * @param functionToRun A consumer that will receive a seneorClass object
     *        initialized with the newest data from the sensor
     * @throws SensorLostRuntimeException */
    public void subscribe(final Consumer<T> functionToRun) throws SensorLostRuntimeException {
        try {
            systemCore.databaseHandler.addListener(sensorId, generateSensorListener(functionToRun, true));
        } catch (SensorNotFoundException e) {
            log.error(LOG_MSG_SENSOR_NOT_FOUND, e);
            throw new SensorLostRuntimeException(e);
        }
    }

    /** Allows registration to a sensor. on time, the sensor will be polled and
     * @param t the time when a polling is requested
     * @param functionToRun A consumer that will receive a seneorClass object
     *        initialized with the newest data from the sensor
     * @param repeat <code>false</code> if you want to query the sensor on the
     *        given time only once, <code>true</code> otherwise (query at this
     *        time FOREVER) */
    public void subscribe(final LocalTime t, final Consumer<T> functionToRun, final Boolean repeat) {
        new Timer().schedule(new QueryTimerTask(sensorId, t, generateSensorListener(functionToRun, true), repeat), localTimeToDate(t));
    }

    /** Request for the latest k entries of data received by a sensor
     * @param numOfEntries the number of entries desired
     * @return the list of the <code> &lt;=k </code> available entries
     * @throws SensorLostRuntimeException */
    public List<T> receiveLastEntries(final int numOfEntries) throws SensorLostRuntimeException {
        try {
            return systemCore.databaseHandler.getList(sensorId).getLastKEntries(numOfEntries).stream()
                    .map(jsonData -> new Gson().fromJson(jsonData, sensorDataClass)).collect(Collectors.toList());
        } catch (SensorNotFoundException e) {
            log.error(LOG_MSG_SENSOR_NOT_FOUND, e);
            throw new SensorLostRuntimeException(e);
        }
    }

    /** Request the latest data received by the sensor in the system
     * @return the latest data or null if none is available
     * @throws SensorLostRuntimeException */
    public T receiveLastEntry() throws SensorLostRuntimeException {
        final List<T> $ = receiveLastEntries(1);
        return $.isEmpty() ? null : $.get(0);
    }

    /** Send a message to a sensor.
     * @param instruction the message that the sensor will receive
     * @throws SensorLostRuntimeException */
    public void instruct(final Map<String, String> instruction) throws SensorLostRuntimeException {
        if (!systemCore.databaseHandler.sensorExists(sensorId)) {
            log.error(LOG_MSG_RUNTIME_THROW + " This is because " + sensorId + " Doesn't exist");
            throw new SensorLostRuntimeException(null);
        }
        systemCore.sensorsHandler.sendInstruction(new UpdateMessage(sensorId, instruction));
    }

    // [start] timer functions
    static Date localTimeToDate(final LocalTime ¢) {
        return Date.from(¢.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
    }

    private class QueryTimerTask extends TimerTask {
        Boolean repeat;
        String sensorId1;
        LocalTime t;
        Consumer<String> notifee;

        QueryTimerTask(final String sensorId1, final LocalTime t, final Consumer<String> notifee, final Boolean repeat) {
            this.repeat = repeat;
            this.notifee = notifee;
            this.t = t;
            this.sensorId1 = sensorId1;
        }

        /* (non-Javadoc)
         * 
         * @see java.util.TimerTask#run() */
        @Override @SuppressWarnings("synthetic-access") public void run() {
            notifee.accept(systemCore.databaseHandler.getLastEntryOf(sensorId1).orElse(new String()));
            if (repeat)
                new Timer().schedule(this, localTimeToDate(t));
        }
    }
    // [end]
}
