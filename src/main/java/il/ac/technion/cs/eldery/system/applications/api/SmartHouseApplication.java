package il.ac.technion.cs.eldery.system.applications.api;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import il.ac.technion.cs.eldery.system.EmergencyLevel;
import il.ac.technion.cs.eldery.system.applications.ApplicationsHandler;
import il.ac.technion.cs.eldery.system.applications.api.exceptions.OnLoadException;
import il.ac.technion.cs.eldery.system.exceptions.SensorNotFoundException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;

/** The API for the apps/modules developers Every app that wants to be installed
 * on the system, MUST extend this class
 * @author RON
 * @author roysh
 * @author Elia Traore
 * @since 8.12.2016 */
public abstract class SmartHouseApplication {
    private ApplicationsHandler applicationsHandler;
    private Node rootNode;

    public SmartHouseApplication() {}

    public void setApplicationsHandler(final ApplicationsHandler ¢) {
        applicationsHandler = ¢;
    }

    public <T extends Initializable> T setContentView(URL location) {
        final FXMLLoader fxmlLoader = new FXMLLoader(location);
        try {
            rootNode = fxmlLoader.load();
        } catch (IOException e) {
            rootNode = null;
            e.printStackTrace();
        }
        return fxmlLoader.getController();
    }
    
    public final Node getRootNode() {
        return rootNode;
    }

    // [start] Public - Services to application developers
    /** Ask for the list of sensorIds registered by a specific commercial name
     * @param sensorCommercialName the sensor in question
     * @return a list of IDs of those sensors in the system. They can be used in
     *         any "sensorId" field in any method */
    public List<String> inquireAbout(final String sensorCommercialName) {
        return applicationsHandler.getDatabaseHandler().getSensors(sensorCommercialName);
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
        applicationsHandler.subscribeToSensor(sensorId, sensorClass, generateConsumer(functionToRun));
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
        applicationsHandler.subscribeToSensor(sensorId, t, sensorClass, generateConsumer(functionToRun), repeat);
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
        return applicationsHandler.querySensor(sensorId, sensorClass, numOfEntries);
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

    /** Report an abnormality in the expected schedule. The system will contact
     * the needed personal, according to the urgency level
     * @param message Specify the abnormality, will be presented to the
     *        contacted personal
     * @param eLevel The level of personnel needed in the situation */
    public final void sendAlert(final String message, final EmergencyLevel eLevel) {
        applicationsHandler.alertOnAbnormalState(message, eLevel);
    }

    /** Send a message to a sensor. 
     * @param sensorId The ID of the sensor, returned from
     *        inquireAbout(sensorCommercialName)
     * @param instruction the message that the sensor will recieve */
    public final void instructSensor(final String sensorId, final Map<String,String> instruction) {
        applicationsHandler.instructSensor(sensorId, instruction);
    }

    /** Saves the app's data to the system's database
     * @param data
     * @return true if the data was saved to the system, or false otherwise */
    @SuppressWarnings("static-method") public final boolean saveToDatabase(final String data) {
        return data != null;
    }

    /** Loads the app's data from the system's database
     * @return a string with the data */
    @SuppressWarnings("static-method") public final String loadFromDatabase() {
        return null;
    }
    // [end]

    // [start] Public abstract - the developer must implement
    /** This will run when the system loads the app. Here all of the sensors
     * subscriptions must occur */
    public abstract void onLoad() throws OnLoadException;

    public abstract String getApplicationName();

    // [end]

    // [start] Private static functions
    /** Surrounds the given function with a Platform.runLater
     * @param functionToRun
     * @return the modified consumer */
    private static <T extends SensorData> Consumer<T> generateConsumer(final Consumer<T> functionToRun) {
        return sensorData -> Platform.runLater(() -> functionToRun.accept(sensorData));
    }
    // [end]

}
