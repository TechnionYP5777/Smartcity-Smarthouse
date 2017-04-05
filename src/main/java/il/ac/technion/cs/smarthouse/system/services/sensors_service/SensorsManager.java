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

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import il.ac.technion.cs.smarthouse.networking.messages.UpdateMessage;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.exceptions.SensorNotFoundException;
import il.ac.technion.cs.smarthouse.system.services.Service;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;

public class SensorsManager extends Service {
    
    static Logger log = Logger.getLogger(SensorsManager.class);
    
    public SensorsManager(final SystemCore $) {
        super($);
    }
    
    static <T extends SensorData> Consumer<String> generateSensorListener(final Class<T> sensorClass, final Consumer<T> functionToRun) {
        return jsonData -> functionToRun.accept(new Gson().fromJson(jsonData, sensorClass));
    }
    
    /** Surrounds the given function with a Platform.runLater
     * @param functionToRun
     * @return the modified consumer */
    protected static <T extends SensorData> Consumer<T> generateConsumer(final Consumer<T> functionToRun, boolean runOnFx) {
        return !runOnFx ? functionToRun : JavaFxHelper.surroundConsumerWithFx(functionToRun);
    }
    
    static Date localTimeToDate(final LocalTime ¢) {
        return Date.from(¢.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
    }
    
    
    
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
            notifee.accept(systemCore.databaseHandler.getLastEntryOf(sensorId).orElse(new String()));
            if (repeat)
                new Timer().schedule(this, localTimeToDate(t));
        }

    }
    
    
    
    
    /** Ask for the list of sensorIds registered by a specific commercial name
     * @param sensorCommercialName the sensor in question
     * @return a list of IDs of those sensors in the system. They can be used in
     *         any "sensorId" field in any method */
    public List<String> inquireAbout(final String sensorCommercialName) {
        return systemCore.databaseHandler.getSensors(sensorCommercialName);
    }

    /** Allows registration to a sensor. on update, the data will be given to
     * the consumer for farther processing
     * @param sensorId The ID of the sensor, returned from
     *        inquireAbout(sensorCommercialName)
     * @param sensorClass The class representing the sensor being listened to,
     *        defined by the developer
     * @param functionToRun A consumer that will receive a seneorClass object
     *        initialized with the newest data from the sensor
     * @throws SensorNotFoundException */
    public final <T extends SensorData> void subscribeToSensor(final String sensorId, final Class<T> sensorClass, final Consumer<T> functionToRun)
            throws SensorNotFoundException {
        systemCore.databaseHandler.addListener(sensorId, generateSensorListener(sensorClass, generateConsumer(functionToRun, true)));
    }

    /** Allows registration to a sensor. on time, the sensor will be polled and
     * @param sensorId The ID of the sensor, returned from
     *        inquireAbout(sensorCommercialName)
     * @param t the time when a polling is requested
     * @param sensorClass The class representing the sensor being listened to,
     *        defined by the developer
     * @param functionToRun A consumer that will receive a seneorClass object
     *        initialized with the newest data from the sensor
     * @param repeat <code>false</code> if you want to query the sensor on the
     *        given time only once, <code>true</code> otherwise (query at this
     *        time FOREVER) */
    public final <T extends SensorData> void subscribeToSensor(final String sensorId, final LocalTime t, final Class<T> sensorClass,
            final Consumer<T> functionToRun, final Boolean repeat) {
        new Timer().schedule(new QueryTimerTask(sensorId, t, generateSensorListener(sensorClass, generateConsumer(functionToRun, true)), repeat), localTimeToDate(t));
    }

    /** Request for the latest k entries of data received by a sensor
     * @param sensorId The ID of the sensor, returned from
     *        inquireAbout(sensorCommercialName)
     * @param sensorClass The class representing the sensor being listened to,
     *        defined by the developer
     * @param numOfEntries the number of entries desired
     * @return the list of the <code> &lt;=k </code> available entries
     * @throws SensorNotFoundException */
    public final <T extends SensorData> List<T> receiveLastEntries(final String sensorId, final Class<T> sensorClass, final int numOfEntries)
            throws SensorNotFoundException {
        return systemCore.databaseHandler.getList(sensorId).getLastKEntries(numOfEntries).stream().map(jsonData -> new Gson().fromJson(jsonData, sensorClass))
                .collect(Collectors.toList());
    }

    /** Request the latest data received by the sensor in the system
     * @param sensorId The ID of the sensor, returned from
     *        inquireAbout(sensorCommercialName)
     * @param sensorClass The class representing the sensor being listened to,
     *        defined by the developer
     * @return the latest data or null if none is available
     * @throws SensorNotFoundException */
    public final <T extends SensorData> T receiveLastEntry(final String sensorId, final Class<T> sensorClass) throws SensorNotFoundException {
        final List<T> $ = receiveLastEntries(sensorId, sensorClass, 1);
        return $.isEmpty() ? null : $.get(0);
    }
    
    /** Send a message to a sensor. 
     * @param sensorId The ID of the sensor, returned from
     *        inquireAbout(sensorCommercialName)
     * @param instruction the message that the sensor will recieve 
     * @throws SensorNotFoundException */
    @SuppressWarnings("boxing")
    public final void instructSensor(final String sensorId, final Map<String,String> instruction) throws SensorNotFoundException {
        if(!systemCore.databaseHandler.sensorExists(sensorId))
            throw new SensorNotFoundException();
        systemCore.sensorsHandler.sendInstruction(new UpdateMessage(sensorId, instruction));
    }

}
