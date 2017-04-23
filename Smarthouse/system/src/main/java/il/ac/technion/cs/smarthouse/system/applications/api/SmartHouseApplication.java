package il.ac.technion.cs.smarthouse.system.applications.api;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.services.Service;
import il.ac.technion.cs.smarthouse.system.services.ServiceManager;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;
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
    private static Logger log = LoggerFactory.getLogger(SmartHouseApplication.class);
    
    private ServiceManager serviceManager;
    private Node rootNode;

    public SmartHouseApplication() {}

    // [start] Public - Services to the SystemCore
    public void setServiceManager(final ServiceManager $) {
        serviceManager = $;
    }

    public final Node getRootNode() {
        return rootNode;
    }
    // [end]

    // [start] Public - Services to application developers
    /** Set the fxml file that will be used
     * @param location of the fxml file
     * @return */
    public <T extends Initializable> T setContentView(URL location) {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(location);
            rootNode = fxmlLoader.load();
            return fxmlLoader.getController();
        } catch (Exception e) {
            rootNode = null;
            log.error("Couldn't load the application's fxml. The rootNode is null", e);
        }
        return null;
    }

    /** Get a service from the system
     * @param $
     * @return */
    public Service getService(ServiceType $) {
        return serviceManager.getService($);
    }

    /** Saves the app's data to the system's database
     * @param data
     * @return true if the data was saved to the system, or false otherwise */
    @SuppressWarnings("static-method") public final boolean saveToDatabase(final String data) {
        log.warn("This function is not implemented yet");
        return data != null;
    }

    /** Loads the app's data from the system's database
     * @return a string with the data */
    @SuppressWarnings("static-method") public final String loadFromDatabase() {
        log.warn("This function is not implemented yet");
        return null;
    }
    // [end]

    // [start] Public abstract - the developer must implement
    /** This will run when the system loads the app. Here all of the sensors
     * subscriptions must occur */
    public abstract void onLoad() throws Exception;

    public abstract String getApplicationName();
    // [end]
}
