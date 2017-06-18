package il.ac.technion.cs.smarthouse.system.services.sensors_service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.file_system.PathBuilder;
import il.ac.technion.cs.smarthouse.system.sensors.SensorLocation;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;
import il.ac.technion.cs.smarthouse.utils.StringConverter;
import il.ac.technion.cs.smarthouse.utils.TimedListener;
import il.ac.technion.cs.smarthouse.utils.UuidGenerator;
import javafx.application.Platform;

import sun.reflect.ReflectionFactory;

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
    private String onSensorMesgRecivedListenerId;
    
    private List<String> onNewAliasListenersId = new ArrayList<>();

    /**
     * This c'tor should be used only by the {@link SensorsService}
     * 
     * @param fileSystem
     *            A reference to the system's fileSystem
     * @param commercialName
     *            The sensor's commercial name
     * @param sensorDataClass
     *            The class representing the sensor being listened to, defined
     *            by the developer
     * @param alias
     *            The sensor's alias
     */
    SensorApiImpl(final FileSystem fileSystem, final String commercialName, final Class<T> sensorDataClass,
                    final String alias) {
        assert fileSystem != null && commercialName != null && sensorDataClass != null;

        this.fileSystem = fileSystem;
        this.commercialName = commercialName;
        this.sensorDataClass = sensorDataClass;

        searchForSensorId(alias);
    }

    /**
     * {@link #SensorApiImpl(FileSystem, String, Class, String)}
     * <p>
     * alias is null by default
     * 
     * @param fileSystem
     * @param commercialName
     * @param sensorDataClass
     */
    SensorApiImpl(final FileSystem fileSystem, final String commercialName, final Class<T> sensorDataClass) {
        this(fileSystem, commercialName, sensorDataClass, null);
    }

    // ===================================================================
    // ======================== getPath functions ========================
    // ===================================================================

    private String getPath_commercialNamePath() {
        assert commercialName != null;
        return FileSystemEntries.COMMERCIAL_NAME.buildPath(commercialName);
    }

    private String getPath_doneMsg(final String sensorId1) {
        assert commercialName != null && sensorId1 != null;
        return FileSystemEntries.DONE_SENDING_MSG.buildPath(commercialName, sensorId1);
    }

    private String getPath_location(final String sensorId1) {
        assert commercialName != null && sensorId1 != null;
        return FileSystemEntries.LOCATION.buildPath(commercialName, sensorId1);
    }

    private String getPath_alias(final String sensorId1) {
        assert commercialName != null && sensorId1 != null;
        return FileSystemEntries.ALIAS.buildPath(commercialName, sensorId1);
    }

    // =====================================================================
    // ======================== Important functions ========================
    // =====================================================================

    /**
     * Creates an instance of the {@link #sensorDataClass} class with the
     * current data from the fileSystem
     * 
     * @return the new instance
     */
    private T createSensorDataObj() {
        assert sensorId != null;

        T sensorData;
        try {
            sensorData = sensorDataClass.cast(ReflectionFactory.getReflectionFactory()
                            .newConstructorForSerialization(sensorDataClass, SensorData.class.getDeclaredConstructor())
                            .newInstance());

            for (final Field field : sensorDataClass.getDeclaredFields())
                if (field.isAnnotationPresent(SystemPath.class)) {
                    field.setAccessible(true);

                    field.set(sensorData, StringConverter.convert(field.getType(),
                                    fileSystem.<String>getData(FileSystemEntries.SENSORS_DATA_FULL__WITH_SENSOR_ID
                                                    .buildPath(field.getAnnotation(SystemPath.class).value(),
                                                                    sensorId))));
                }
        } catch (InstantiationException | IllegalArgumentException | IllegalAccessException | SecurityException
                        | InvocationTargetException | NoSuchMethodException e) {
            log.error("SensorApi's OnSensorMsgRecived subscriber has failed! - commercialName: \"" + getCommercialName()
                            + "\" | sensorId: \"" + sensorId + "\"", e);
            return null;
        }

        sensorData.sensorLocation = getSensorLocation();
        sensorData.commercialName = getCommercialName();
        sensorData.sensorAlias = getSensorAlias();

        return sensorData;
    }

    /**
     * Searches the file system ({@link FileSystem}) for a valid sensor ID
     * <p>
     * A valid sensor ID represents a sensor that has connected to the system
     * (at some point in the past), and has a commercial name of
     * {@link #commercialName}. <br>
     * The sensor should also have the alias `alias`. If `alias` is null than
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
     * @param alias
     * @return true if a sensor ID was found (or if it was already set)
     */
    private boolean searchForSensorId(String alias) {
        if (sensorId != null)
            return true;

        final String commNamePath = getPath_commercialNamePath();

        for (final String sensorId1 : fileSystem.getChildren(commNamePath)) {
            final String aliasPath = getPath_alias(sensorId1);
            if (alias == null || fileSystem.wasPathInitiated(aliasPath)
                            && alias.equals(fileSystem.<String>getData(aliasPath))) {

                // set the sensor's ID
                sensorId = sensorId1;
                log.info("\n\tSensorApiImpl: SID found\n\tSensor ID found: " + sensorId + "\n\tCommercial name: "
                                + commercialName);

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
                                FileSystemEntries.LISTENERS_OF_SENSOR.buildPath(commercialName, sensorId, basePath)));

                return true;
            }
        }

        if (sensorIdListenerId == null)
            sensorIdListenerId = fileSystem.subscribe((path, data) -> searchForSensorId(alias), commNamePath);
        return false;
    }

    /**
     * Removes all of this API's event handlers from the fileSystem.
     */
    private void disconnectSensor() {
        if (sensorIdListenerId != null)
            fileSystem.unsubscribe(sensorIdListenerId);
        if (onSensorMesgRecivedListenerId != null)
            fileSystem.unsubscribe(onSensorMesgRecivedListenerId);
        sensorId = null;
        sensorIdListenerId = null;
        onSensorMesgRecivedListenerId = null;
    }

    // ======================================================================
    // ==================== Generate Listeners functions ====================
    // ======================================================================

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

    // ======================================================================
    // =================== Overridden SensorApi functions ===================
    // ======================================================================

    @Override
    public String getSensorLocation() {
        if (sensorId == null)
            return SensorLocation.UNDIFINED;
        return Optional.ofNullable(fileSystem.getData(getPath_location(sensorId))).orElse(SensorLocation.UNDIFINED)
                        + "";
    }

    @Override
    public String getCommercialName() {
        return commercialName;
    }

    @Override
    public String getSensorAlias() {
        return sensorId != null ? fileSystem.getData(getPath_alias(sensorId)) : "";
    }

    @Override
    public String subscribe(final Consumer<T> functionToRun) {
        final String id = UuidGenerator.GenerateUniqueIDstring();
        functionsToRunOnMessageRecived.put(id, generateListener_WithoutSensorDataCreation(functionToRun, true));
        return id;
    }

    @Override
    public void unsubscribe(final String listenerId) {
        functionsToRunOnMessageRecived.remove(listenerId);
        Optional.of(functionsToRunOnTime.remove(listenerId)).ifPresent(tl -> tl.kill());
        if (onNewAliasListenersId.remove(listenerId))
            fileSystem.unsubscribe(listenerId);
    }

    @Override
    public void instruct(final String instruction, final String... path) {
        final String basePath = PathBuilder.buildPath(path);
        if (sensorId == null)
            instructionsQueue.put(basePath, instruction);
        else
            fileSystem.sendMessage(instruction,
                            FileSystemEntries.LISTENERS_OF_SENSOR.buildPath(commercialName, sensorId, basePath));
    }

    @Override
    public boolean isConnected() {
        return sensorId != null;// TODO: should also check via the systemCore
                                // (or FS) that the sensor is still connected...
    }

    @Override
    public void reselectSensorByAlias(String alias) {
        if (alias == null)
            return;
        log.info("SensorApi: reselectSensorByAlias: " + alias);
        functionsToRunOnTime.values().forEach(TimedListener::kill);
        disconnectSensor();
        searchForSensorId(alias);
    }

    // ===================================================================
    // ==================== subscribeOnTime functions ====================
    // ===================================================================

    private String subscribeOnTimeAux(final Consumer<T> functionToRun, final LocalTime timeToStartOn,
                    final Long repeatInMillisec) {
        final TimedListener tl = new TimedListener(generateListener_WithSensorDataCreation(functionToRun, true),
                        timeToStartOn, repeatInMillisec);
        final String id = UuidGenerator.GenerateUniqueIDstring();
        functionsToRunOnTime.put(id, tl);
        if (sensorId != null)
            tl.start();
        return id;
    }

    @Override
    public String subscribeOnTime(final Consumer<T> functionToRun, final LocalTime timeToStartOn) {
        return subscribeOnTimeAux(functionToRun, timeToStartOn, null);
    }

    @Override
    public String subscribeOnTime(final Consumer<T> functionToRun, final LocalTime timeToStartOn,
                    final long milliseconds) {
        return subscribeOnTimeAux(functionToRun, timeToStartOn, milliseconds);
    }

    @Override
    public String subscribeOnTime(final Consumer<T> functionToRun, final long milliseconds) {
        return subscribeOnTimeAux(functionToRun, null, milliseconds);
    }

    @Override
    public String runWhenSensorIsFound(final Consumer<T> functionToRun) {
        return subscribeOnTimeAux(functionToRun, null, null);
    }

    // ======================================================================
    // ================= commercialName's aliases functions =================
    // ======================================================================

    @Override
    public List<String> getAllAliases() {
        final List<String> l = new ArrayList<>();
        for (String sensorId1 : fileSystem.getChildren(FileSystemEntries.COMMERCIAL_NAME.buildPath(commercialName)))
            l.add(fileSystem.getData(FileSystemEntries.ALIAS.buildPath(commercialName, sensorId1)));
        return l;
    }

    @Override
    public String listenForNewAliases(Consumer<String> functionToRun) {
        final String id = fileSystem.subscribe((path, data) -> {
            if (FileSystemEntries.ALIAS.isValidPath(path))
                JavaFxHelper.surroundConsumerWithFx(functionToRun).accept(data + "");
        }, getPath_commercialNamePath());
        
        onNewAliasListenersId.add(id);
        
        return id;
    }
}
