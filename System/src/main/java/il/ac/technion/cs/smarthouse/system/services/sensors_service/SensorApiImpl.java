package il.ac.technion.cs.smarthouse.system.services.sensors_service;

import java.lang.reflect.Field;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.SensorLocation;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.file_system.PathBuilder;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;
import il.ac.technion.cs.smarthouse.utils.StringConverter;
import il.ac.technion.cs.smarthouse.utils.UuidGenerator;

/**
 * An API class for the developers, that allows interactions with a specific
 * sensor.
 * 
 * @author RON
 * @since 07-04-2017
 * @param <T>
 *            the sensor's messages will be deserialize into this class. T must
 *            extend SensorData
 */
final class SensorApiImpl<T extends SensorData> implements SensorApi<T> {
    private static Logger log = LoggerFactory.getLogger(SensorApiImpl.class);

    private final FileSystem fileSystem;

    private final String commercialName;
    private String sensorId;
    private final SensorLocation defaultLocation;
    private final Class<T> sensorDataClass;

    private final Map<String, Consumer<T>> functionsToRunOnMessageRecived = new HashMap<>();
    private final Map<String, TimedListener> functionsToRunOnTime = new HashMap<>();
    private final Map<String, String> instructionsQueue = new HashMap<>();

    /**
     * This c'tor should be used only by the {@link SensorsService}
     * 
     * @param systemCore
     *            a reference to the system's core
     * @param sensorId
     *            The ID of the sensor, returned from
     *            inquireAbout(sensorCommercialName)
     * @param sensorDataClass
     *            The class representing the sensor being listened to, defined
     *            by the developer
     */
    SensorApiImpl(final FileSystem fileSystem, final String commercialName, final SensorLocation defaultLocation,
                    final Class<T> sensorDataClass) {
        this.fileSystem = fileSystem;
        this.commercialName = commercialName;
        this.defaultLocation = defaultLocation != null ? defaultLocation : SensorLocation.UNDEFINED;
        this.sensorDataClass = sensorDataClass;
        searchForSensorId();
    }

    private String getPath_commercialNamePath() {
        assert commercialName != null;
        return PathBuilder.buildPath(FileSystemEntries.SENSORS, commercialName);
    }

    private String getPath_sensorId(String sensorId1) {
        assert sensorId1 != null;
        return PathBuilder.buildPath(getPath_commercialNamePath(), sensorId1);
    }

    private String getPath_doneMsg(String sensorId1) {
        return PathBuilder.buildPath(getPath_sensorId(sensorId1), FileSystemEntries.DONE_SENDING_MSG);
    }

    private String getPath_location(String sensorId1) {
        return PathBuilder.buildPath(getPath_sensorId(sensorId1), FileSystemEntries.LOCATION);
    }

    private String sensorIdListenerId;
    private String onSensorMesgRecivedListenerId;

    private T createSensorDataObj() {
        assert sensorId != null;

        T sensorData;
        try {
            sensorData = sensorDataClass.newInstance();

            for (Field field : sensorDataClass.getDeclaredFields())
                if (field.isAnnotationPresent(PathVal.class)) {
                    field.setAccessible(true);
                    
                    field.set(sensorData, StringConverter.convert(field.getType(), fileSystem.<String>getData(PathBuilder
                                    .buildPathForSensorsData(field.getAnnotation(PathVal.class).value(), sensorId))));
                }
        } catch (InstantiationException | IllegalArgumentException | IllegalAccessException | SecurityException e) {
            log.error("SensorApi's OnSensorMsgRecived subscriber has failed! - commercialName = " + getCommercialName()
                            + " sensorId = " + sensorId, e);
            return null;
        }

        sensorData.sensorLocation = getSensorLocation();
        sensorData.commercialName = getCommercialName();

        return sensorData;
    }

    /**
     * Searches the file system ({@link FileSystem}) for a valid sensor ID
     * <p>
     * A valid sensor ID represents a sensor that has connected to the system
     * (at some point in the past), and has a commercial name of
     * {@link #commercialName}. <br>
     * The sensor should also be found at {@link #defaultLocation}. If
     * {@link #defaultLocation} is null or {@link SensorLocation#UNDEFINED} than
     * any sensor with the given {@link #commercialName} can be selected.
     * <p>
     * If an ID is not found, a listener will be placed on the file system under
     * the given {@link #commercialName}. <br>
     * The listener will run this function every time it is invoked. <br>
     * When a sensor ID is found, the listener is unsubscribed from the file
     * system.
     * <p>
     * When a sensor ID is found, a new listener is placed under the
     * {@link FileSystemEntries#DONE_SENDING_MSG} entry.
     * 
     * @return true if a sensor ID was found (or if it was already set)
     */
    private boolean searchForSensorId() {
        if (sensorId != null)
            return true;

        final String commNamePath = getPath_commercialNamePath();

        for (String sensorId1 : fileSystem.getChildren(commNamePath)) {
            final String locationPath = getPath_location(sensorId1);
            if (defaultLocation == null || defaultLocation == SensorLocation.UNDEFINED
                            || (fileSystem.wasPathInitiated(locationPath)
                                            && fileSystem.<SensorLocation>getData(locationPath) == defaultLocation)) {

                // set the sensor's ID
                sensorId = sensorId1;
                log.info("SID found: " + sensorId);

                // unsubscribe from the sensorIdListener
                Optional.ofNullable(sensorIdListenerId).ifPresent(id -> fileSystem.unsubscribe(id, commNamePath));

                // subscribe on the "done" entry
                onSensorMesgRecivedListenerId = fileSystem.subscribe((path, data) -> {
                    if (functionsToRunOnMessageRecived.isEmpty())
                        return;

                    Optional.ofNullable(createSensorDataObj()).ifPresent(sensorData -> functionsToRunOnMessageRecived
                                    .values().forEach(functionToRun -> functionToRun.accept(sensorData)));

                }, getPath_doneMsg(sensorId));

                // initiate the functions that work with time
                functionsToRunOnTime.values().forEach(TimedListener::start);

                // send instructions
                instructionsQueue.forEach((basePath, instruction) -> fileSystem.sendMessage(instruction,
                                PathBuilder.buildPathForSensorsData(basePath, sensorId)));

                return true;
            }
        }

        if (sensorIdListenerId == null)
            sensorIdListenerId = fileSystem.subscribe((path, data) -> searchForSensorId(), commNamePath);
        return false;
    }

    @Override
    public SensorLocation getSensorLocation() {
        return sensorId == null ? SensorLocation.UNDEFINED : fileSystem.getData(getPath_location(sensorId));
    }

    @Override
    public String getCommercialName() {
        return commercialName;
    }

    /**
     * Wraps the <code>functionToRun</code> Consumer with helpful wrappers.
     * <p>
     * 1. A wrapper that sets the <code>sensorData</code>'s
     * <code>sensorLocation</code>
     * <p>
     * 2. Surrounds the given function with a Platform.runLater, if
     * <code>runOnFx == true</code>
     * 
     * @param functionToRun
     *            a Consumer that receives <code>SensorData sensorData</code>
     *            and operates on it.
     * @param runOnFx
     * @return the modified consumer [[SuppressWarningsSpartan]]
     */
    private Consumer<T> generateListener_WithoutSensorDataCreation(final Consumer<T> functionToRun,
                    final boolean runOnFx) {
        return !runOnFx ? functionToRun : JavaFxHelper.surroundConsumerWithFx(functionToRun);
    }

    private Runnable generateListener_WithSensorDataCreation(final Consumer<T> functionToRun, final boolean runOnFx) {
        return () -> generateListener_WithoutSensorDataCreation(functionToRun, runOnFx).accept(createSensorDataObj());
    }

    /**
     * Allows registration to a sensor. on update, the data will be given to the
     * consumer for farther processing
     * 
     * @param functionToRun
     *            A consumer that will receive a seneorClass object initialized
     *            with the newest data from the sensor
     * @throws SensorLostRuntimeException
     */
    @Override
    public String subscribe(final Consumer<T> functionToRun) {
        final String id = UuidGenerator.GenerateUniqueIDstring();
        functionsToRunOnMessageRecived.put(id, generateListener_WithoutSensorDataCreation(functionToRun, true));
        return id;
    }

    @Override
    public void unsubscribe(String listenerId) {
        functionsToRunOnMessageRecived.remove(listenerId);
        Optional.of(functionsToRunOnTime.remove(listenerId)).ifPresent(tl -> tl.kill());
    }
    
    

    /**
     * Allows registration to a sensor. on time, the sensor will be polled and
     * 
     * @param t
     *            the time when a polling is requested
     * @param functionToRun
     *            A consumer that will receive a seneorClass object initialized
     *            with the newest data from the sensor
     * @param repeat
     *            <code>false</code> if you want to query the sensor on the
     *            given time only once, <code>true</code> otherwise (query at
     *            this time FOREVER)
     */
    private String subscribeOnTimeAux(final Consumer<T> functionToRun, final LocalTime timeToStartOn, final Long repeatInMilisec) {
        final TimedListener tl = new TimedListener(generateListener_WithSensorDataCreation(functionToRun, true), timeToStartOn, repeatInMilisec);
        final String id = UuidGenerator.GenerateUniqueIDstring();
        functionsToRunOnTime.put(id, tl);
        if (sensorId != null)
            tl.start();
        return id;
    }

    /**
     * Send a message to a sensor.
     * 
     * @param instruction
     *            the message that the sensor will receive
     */
    @Override
    public void instruct(final String instruction, final String... path) {
        final String basePath = PathBuilder.buildPath(path);
        if (sensorId == null)
            instructionsQueue.put(basePath, instruction);
        else
            fileSystem.sendMessage(instruction, PathBuilder.buildPathForSensorsData(basePath, sensorId));
    }

    // [start] timer functions
    private static class TimedListener {
        private static Date localTimeToDate(final LocalTime $) {
            return Date.from($.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
        }

        final Runnable notifee;
        final LocalTime timeToStartOn;
        final Long repeatInMilisec;
        Timer currentTimer;

        public TimedListener(final Runnable notifee, final LocalTime timeToStartOn, final Long repeatInMilisec) {
            this.notifee = notifee;
            this.timeToStartOn = timeToStartOn;
            this.repeatInMilisec = repeatInMilisec;
        }

        public void start() {
            if (currentTimer != null)
                return;
            
            TimerTask t = new TimerTask() {
                @Override
                public void run() {
                    notifee.run();
                }
            };
            
            currentTimer = new Timer();
            
            if (repeatInMilisec == null && timeToStartOn != null)
                currentTimer.schedule(t, localTimeToDate(timeToStartOn));
            else if (repeatInMilisec != null && timeToStartOn != null)
                currentTimer.schedule(t, localTimeToDate(timeToStartOn), repeatInMilisec);
            else if (repeatInMilisec != null && timeToStartOn == null)
                currentTimer.schedule(t, localTimeToDate(LocalTime.now()), repeatInMilisec);
        }

        public void kill() {
            if (currentTimer != null)
                currentTimer.cancel();
            currentTimer = null;
        }
    }
    // [end]

    @Override
    public boolean isConnected() {
        return sensorId != null;// TODO: should also check via the systemCore
                                // (or FS) that the sensor is still connected...
    }

    @Override
    public String subscribeOnTime(Consumer<T> functionToRun, LocalTime timeToStartOn) {
        return subscribeOnTimeAux(functionToRun, timeToStartOn, null);
    }

    @Override
    public String subscribeOnTime(Consumer<T> functionToRun, LocalTime timeToStartOn, long miliseconds) {
        return subscribeOnTimeAux(functionToRun, timeToStartOn, miliseconds);
    }

    @Override
    public String subscribeOnTime(Consumer<T> functionToRun, long miliseconds) {
        return subscribeOnTimeAux(functionToRun, null, miliseconds);
    }
    
//    public void close() throws Exception {
//        if (sensorIdListenerId != null)
//            fileSystem.unsubscribe(sensorIdListenerId, getPath_commercialNamePath());
//        if (onSensorMesgRecivedListenerId != null)
//            fileSystem.unsubscribe(onSensorMesgRecivedListenerId, getPath_doneMsg(sensorId));
//    }
}
