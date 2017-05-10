package il.ac.technion.cs.smarthouse.system;

import com.google.gson.annotations.Expose;

import il.ac.technion.cs.smarthouse.system.applications.ApplicationsCore;
import il.ac.technion.cs.smarthouse.system.sensors.SensorsHandler;
import il.ac.technion.cs.smarthouse.system.services.ServiceManager;
import il.ac.technion.cs.smarthouse.system.user_information.UserInformation;

/** Hold the databases of the smart house, and allow sensors and applications to
 * store and read information about the changes in the environment */
public class SystemCore implements Savable {

    public final ServiceManager serviceManager = new ServiceManager(this);
    public final DatabaseHandler databaseHandler = new DatabaseHandler();
    public final SensorsHandler sensorsHandler = new SensorsHandler(databaseHandler);
    @Expose public final ApplicationsCore applicationsHandler = new ApplicationsCore(this);
    protected UserInformation user;
    private boolean userInitialized;

    public void initializeSystemComponents() {
        System.out.println("Initializing system components...");
        new Thread(sensorsHandler).start();
    }

    public UserInformation getUser() {
        return user;
    }

    public void initializeUser(final String name, final String id, final String phoneNumber, final String homeAddress) {
        user = new UserInformation(name, id, phoneNumber, homeAddress);
        userInitialized = true;
    }

    public boolean isUserInitialized() {
        return userInitialized;
    }

    public void shutdown() {
        sensorsHandler.closeSockets();
    }

}
