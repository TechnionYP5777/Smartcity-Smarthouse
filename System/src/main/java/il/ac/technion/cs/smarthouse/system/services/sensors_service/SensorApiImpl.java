package il.ac.technion.cs.smarthouse.system.services.sensors_service;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.SensorLocation;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.file_system.PathBuilder;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;
import il.ac.technion.cs.smarthouse.utils.StringConverter;
import il.ac.technion.cs.smarthouse.utils.TimedListener;
import il.ac.technion.cs.smarthouse.utils.UuidGenerator;
import javafx.application.Platform;

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

    // a reference to the system's fileSystem
    private final FileSystem fileSystem;

    // the commercial name of the relevant sensor. musn't be null, given in the
    // c'tor
    private final String commercialName;

    // the sensor's ID. starts as null, until it is found by searchForSensorId()
    // or the fileSystem
    private String sensorId;

    // the sensor's preferred location. the selected sensor will be located at
    // this location. if null or UNDEFINED, the location is ignored, and only
    // the commercial name will affect the sensor selection process
    private final SensorLocation defaultLocation;

    // the class that extends SensorData. the developer will receive information
    // from the sensor via this class
    private final Class<T> sensorDataClass;

    // a buffer for subscribed functions
    private final Map<String, Consumer<T>> functionsToRunOnMessageRecived = new HashMap<>();

    // a buffer for timed listeners
    private final Map<String, TimedListener> functionsToRunOnTime = new HashMap<>();

    // an instructions buffer
    private final Map<String, String> instructionsQueue = new HashMap<>();

    // the ID of the fileSystem's listener on: sensors.commercialName
    // it is removed from the fileSystem after a sensor is found
    private String sensorIdListenerId;

    // the ID of the fileSystem's listener on:
    // sensors.commercialName.sensorId.msg_recevied
    @SuppressWarnings("unused") private String onSensorMesgRecivedListenerId;

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
        assert fileSystem != null && commercialName != null && sensorDataClass != null;

        this.fileSystem = fileSystem;
        this.commercialName = commercialName;
        this.defaultLocation = defaultLocation != null ? defaultLocation : SensorLocation.UNDEFINED;
        this.sensorDataClass = sensorDataClass;

        searchForSensorId();
    }

    // ===================================================================
    // ======================== getPath functions ========================
    // ===================================================================

    private String getPath_commercialNamePath() {
        assert commercialName != null;
        return FileSystemEntries.COMMERCIAL_NAME.buildPath(commercialName);
    }

    private String getPath_doneMsg(String sensorId1) {
        assert commercialName != null && sensorId1 != null;
        return FileSystemEntries.DONE_SENDING_MSG.buildPath(commercialName, sensorId1);
    }

    private String getPath_location(String sensorId1) {
        assert commercialName != null && sensorId1 != null;
        return FileSystemEntries.LOCATION.buildPath(commercialName, sensorId1);
    }

    // =====================================================================
    // ======================== Important functions ========================
    // =====================================================================

    private T createSensorDataObj() {
        assert sensorId != null;

        T sensorData;
        try {
            sensorData = sensorDataClass.newInstance();

            for (Field field : sensorDataClass.getDeclaredFields())
                if (field.isAnnotationPresent(SystemPath.class)) {
                    field.setAccessible(true);

                    field.set(sensorData,
                                    StringConverter.convert(field.getType(),
                                                    fileSystem.<String>getData(FileSystemEntries.SENSORS_DATA.buildPath(
                                                                    field.getAnnotation(SystemPath.class)
                                                                                    .value(),
                                                                    sensorId))));
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
                Optional.ofNullable(sensorIdListenerId).ifPresent(id -> fileSystem.unsubscribe(id));

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
                                FileSystemEntries.SENSORS_DATA.buildPath(basePath, sensorId)));

                return true;
            }
        }

        if (sensorIdListenerId == null)
            sensorIdListenerId = fileSystem.subscribe((path, data) -> searchForSensorId(), commNamePath);
        return false;
    }

    // ==============================================================================
    // ======================== Generate Listeners functions
    // ========================
    // ==============================================================================

    /**
     * Wraps the <code>functionToRun</code> Consumer with a javaFx wrapper.
     * <p>
     * Surrounds the given function with a {@link Platform#runLater(Runnable)},
     * if <code>runOnFx == true</code>
     * 
     * @param functionToRun
     *            a Consumer that receives <code>SensorData sensorData</code>
     *            and operates on it.
     * @param runOnFx
     * @return the modified consumer
     */
    private Consumer<T> generateListener_WithoutSensorDataCreation(final Consumer<T> functionToRun,
                    final boolean runOnFx) {
        return !runOnFx ? functionToRun : JavaFxHelper.surroundConsumerWithFx(functionToRun);
    }

    /**
     * Wraps the <code>functionToRun</code> Consumer with a javaFx wrapper, and
     * then with a sensorData generator.
     * <p>
     * The top wrapper sets the SensorData object, and the second is
     * {@link SensorApiImpl#generateListener_WithoutSensorDataCreation(Consumer, boolean)}
     * 
     * @param functionToRun
     *            a Consumer that receives <code>SensorData sensorData</code>
     *            and operates on it.
     * @param runOnFx
     * @return the modified consumer
     */
    private Runnable generateListener_WithSensorDataCreation(final Consumer<T> functionToRun, final boolean runOnFx) {
        return () -> generateListener_WithoutSensorDataCreation(functionToRun, runOnFx).accept(createSensorDataObj());
    }

    // ================================================================================
    // ======================== Overridden SensorApi functions
    // ========================
    // ================================================================================

    @Override
    public SensorLocation getSensorLocation() {
        return sensorId == null ? SensorLocation.UNDEFINED : fileSystem.getData(getPath_location(sensorId));
    }

    @Override
    public String getCommercialName() {
        return commercialName;
    }

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

    @Override
    public void instruct(final String instruction, final String... path) {
        final String basePath = PathBuilder.buildPath(path);
        if (sensorId == null)
            instructionsQueue.put(basePath, instruction);
        else
            fileSystem.sendMessage(instruction, FileSystemEntries.SENSORS_DATA.buildPath(basePath, sensorId));
    }

    @Override
    public boolean isConnected() {
        return sensorId != null;// TODO: should also check via the systemCore
                                // (or FS) that the sensor is still connected...
    }

    // ===========================================================================
    // ======================== subscribeOnTime functions
    // ========================
    // ===========================================================================

    private String subscribeOnTimeAux(final Consumer<T> functionToRun, final LocalTime timeToStartOn,
                    final Long repeatInMilisec) {
        final TimedListener tl = new TimedListener(generateListener_WithSensorDataCreation(functionToRun, true),
                        timeToStartOn, repeatInMilisec);
        final String id = UuidGenerator.GenerateUniqueIDstring();
        functionsToRunOnTime.put(id, tl);
        if (sensorId != null)
            tl.start();
        return id;
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

    // =================================================================
    // ======================== other functions ========================
    // =================================================================

    // public void close() throws Exception {
    // if (sensorIdListenerId != null)
    // fileSystem.unsubscribe(sensorIdListenerId, getPath_commercialNamePath());
    // if (onSensorMesgRecivedListenerId != null)
    // fileSystem.unsubscribe(onSensorMesgRecivedListenerId,
    // getPath_doneMsg(sensorId));
    // }
}
