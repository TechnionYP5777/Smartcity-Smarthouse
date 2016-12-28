package il.ac.technion.cs.eldery.system.applications.api;

import java.util.List;
import java.util.function.Consumer;

import il.ac.technion.cs.eldery.system.SystemCore;
import il.ac.technion.cs.eldery.system.applications.ApplicationsHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/** The API for the apps/modules developers Every app that wants to be installed
 * on the system, MUST extend this class
 * @author RON
 * @author roysh
 * @since 8.12.2016 */
public abstract class SmartHouseApplication extends Application {
    private ApplicationsHandler applicationsHandler;
    private Stage firstStage;

    public SmartHouseApplication() {}

    public void setApplicationsHandler(ApplicationsHandler h) {
        this.applicationsHandler = h;
    }

    /** This will run when the system loads the app.
     * Here all of the sensors subscriptions must occur */
    public abstract void onLoad();
    
    @SuppressWarnings("static-method")
    public String getIcon() {
        return "";
    }
    
    @Override public void start(Stage s) throws Exception {
        firstStage = s;
        firstStage.setOnCloseRequest(event -> {
            event.consume();
            ((Stage)event.getSource()).setIconified(true);
            });
    }

    public final void reopen() {
        Platform.runLater(() -> firstStage.setIconified(false));
    }
    
    /** Ask for the list of sensorIds registered by a specific commercial name
     * @param sensorCommercialName the sensor in question
     * @return a list of IDs of those sensors in the system. They can be used in
     *         any "sensorId" field in any method */
    public List<String> InqireAbout(final String sensorCommercialName) {
        return applicationsHandler.getDatabaseHandler().getSensors(sensorCommercialName);
    }
    
    public final <T extends SensorData> void subscribeToSensor(final String sensorId, 
            final Class<T> sensorClass, final Consumer<T> functionToRun) {
        //TODO: Elia - call some applicationsHandler function to subscribe. like:
        //applicationsHandler.AddListener(...)
    }
    
    public final <T extends SensorData> List<T> receiveLastEntries(final String sensorId, 
            final Class<T> sensorClass, final int numOfEntries) {
      //TODO: Elia - call some applicationsHandler function
        return null;
    }
    
    public final <T extends SensorData> T receiveLastEntry(final String sensorId, final Class<T> sensorClass) {
      //TODO: Elia - call some applicationsHandler function
        return null;
    }

    public final void sendAlert(String msg) {
      //TODO: call some applicationsHandler function
    }
    
    
    
    
    
//    /** Check if the sensor specified is active in the house
//     * @param sensorID
//     * @return true if the system has this sensor or false otherwise */
//    @SuppressWarnings("static-method") public final boolean checkIfSensorExists(final String sensorCommercialName) {
//        return sensorCommercialName != null;
//    }
//
//    /** Saves the app's data to the system's database
//     * @param data
//     * @return true if the data was saved to the system, or false otherwise */
//    @SuppressWarnings("static-method") public final boolean saveToDatabase(final String data) {
//        return data != null;
//    }
//
//    /** Loads the app's data from the system's database
//     * @return a JSObject with the data */
//    @SuppressWarnings("static-method") public final String loadFromDatabase() {
//        return null;
//    }
}
